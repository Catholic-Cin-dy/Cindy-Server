package com.app.cindy.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.app.cindy.constants.CommonResponseStatus.S3_DELETE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

        @Value("${cloud.aws.region.static}")
        private String region;

    @PostConstruct
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public CompletableFuture<String> uploadFile(MultipartFile multipartFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
                File file = convertMultiPartToFile(multipartFile);
                String fileName = createFileName(multipartFile.getOriginalFilename());
                InputStream inputStream = multipartFile.getInputStream();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(multipartFile.getSize());
                objectMetadata.setContentType(multipartFile.getContentType());
                PutObjectRequest request = new PutObjectRequest(bucket + "/post/image",fileName ,inputStream, objectMetadata);
                // 업로드 시도
                Upload upload = transferManager.upload(request);
                upload.waitForCompletion();
                file.delete();  // 로컬에 임시 저장된 파일 삭제
                return fileName;
            } catch (Exception e) {
                throw new RuntimeException("파일 업로드 실패: " + multipartFile.getOriginalFilename(), e);
            }
        });
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public List<String> upload(List<MultipartFile> multipartFile) {
        List<String> imgUrlList = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
        for (MultipartFile file : multipartFile) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                uploadOnS3(fileName,inputStream,objectMetadata);
                imgUrlList.add(s3Client.getUrl(bucket + "/post/image", fileName).toString());
            } catch (IOException e) {
                //throw new PrivateException(Code.IMAGE_UPLOAD_ERROR);
            }
        }
        LocalDateTime now = LocalDateTime.now();
        Long longTime = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("버킷에 업로드 전 : " + longTime);
        return imgUrlList;
    }

    private void uploadOnS3(String fileName, InputStream inputStream, ObjectMetadata objectMetadata) {
        // AWS S3 전송 객체 생성
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        // 요청 객체 생성
        PutObjectRequest request = new PutObjectRequest(bucket + "/post/image", fileName,inputStream, objectMetadata);
        // 업로드 시도
        Upload upload =  transferManager.upload(request);

        try {
            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {
            log.error(amazonClientException.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    // 이미지파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            //throw new PrivateException(Code.WRONG_INPUT_IMAGE);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            //throw new PrivateException(Code.WRONG_IMAGE_FORMAT);
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void fileDelete(String fileUrl) throws BaseException {
        try{
            String fileKey = fileUrl.substring(53);
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();

            try {
                System.out.println("File Delete : " + fileKey);
                s3.deleteObject(bucket, fileKey);
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }

            System.out.println(String.format("[%s] deletion complete", fileKey));

        } catch (Exception exception) {
            throw new BadRequestException(S3_DELETE_ERROR);
        }
    }

    public void uploadImg(MultipartFile file) {

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket + "/post/image", fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                //throw new PrivateException(Code.IMAGE_UPLOAD_ERROR);
            }
        }


}


