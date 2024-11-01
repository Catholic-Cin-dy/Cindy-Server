package com.app.cindy.service;

import com.app.cindy.domain.board.*;
import com.app.cindy.convertor.BoardConvertor;
import com.app.cindy.domain.board.BoardImg;
import com.app.cindy.domain.board.BoardImgTag;
import com.app.cindy.domain.pk.BoardLikePk;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardReq;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.repository.*;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.repository.BoardImgRepository;
import com.app.cindy.repository.BoardImgTagRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.app.cindy.constants.CommonResponseStatus.NOT_EXIST_BOARD;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    private final BoardImgTagRepository boardImgTagRepository;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardHashTagRepository boardHashTagRepository;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PageResponse<List<BoardRes.BoardList>> getBoardList(Integer sort, Integer page, Integer size, Long userId, UserReq.Distance distance) {
        Pageable pageReq = PageRequest.of(page, size);
        List<BoardRes.BoardList> boardList = new ArrayList<>();
        Page<BoardRepository.GetBoardList> board = null;
        if(sort==0){
            board = boardRepository.findAllBoardByCreate(userId,pageReq);
        }
        else if(sort==1){
            board = boardRepository.findAllBoardByLike(userId,pageReq);
        }else{
            board = boardRepository.findAllBoardByDistance(userId,pageReq,distance.getLatitude(),distance.getLongitude());
        }
        board.forEach(
                result -> boardList.add(
                        new BoardRes.BoardList(
                                result.getBoardId(),
                                result.getTitle(),
                                result.getWriter(),
                                Stream.of(result.getBoardImg().split(",")).collect(Collectors.toList()),
                                result.getProfileImg(),
                                result.getLikeCnt(),
                                result.getCommentCnt(),
                                result.getLikeCheck(),
                                result.getBoardTime()
                                )
                )
        );

        return new PageResponse<>(board.isLast(),boardList);
    }

    public PageResponse<List<BoardRes.BoardList>> getMyBoardList(Integer page, Integer size, Long userId, UserReq.Distance distance) {
        Pageable pageReq = PageRequest.of(page, size);
        List<BoardRes.BoardList> boardList = new ArrayList<>();
        Page<BoardRepository.GetBoardList> board = null;
        board = boardRepository.findAllMyBoard(userId,pageReq);
        board.forEach(
                result -> boardList.add(
                        new BoardRes.BoardList(
                                result.getBoardId(),
                                result.getTitle(),
                                result.getWriter(),
                                Stream.of(result.getBoardImg().split(",")).collect(Collectors.toList()),
                                result.getProfileImg(),
                                result.getLikeCnt(),
                                result.getCommentCnt(),
                                result.getLikeCheck(),
                                result.getBoardTime()
                        )
                )
        );

        return new PageResponse<>(board.isLast(),boardList);
    }

    public BoardRes.BoardDetail getBoardDetail(Long userId, Long boardId) {
        List<BoardImg> boardImg = boardImgRepository.findByBoardId(boardId);
        System.out.println(boardImg.stream().map(e->e.getImgUrl()).collect(Collectors.toList()));
        List<BoardRes.ImgList> imgList = new ArrayList<>();
        BoardRepository.GetBoardDetail board = boardRepository.getBoardDetail(userId,boardId);

        if(board.getBoardId()==null){
            throw new BadRequestException(NOT_EXIST_BOARD);
        }

        for (BoardImg img : boardImg){
            List<BoardImgTag> boardImgTag = boardImgTagRepository.findByImgId(img.getId());
            List<BoardRes.ImgTagList> imgTagList=new ArrayList<>();

            for (BoardImgTag tag : boardImgTag) {
                if(Objects.equals(img.getId(), tag.getImgId())) {
                    BoardRes.ImgTagList imgTag=BoardRes.ImgTagList.builder()
                            .imgId(tag.getImgId())
                            .brandId(tag.getBrandId())
                            .brandName(tag.getBrand().getName())
                            .x(tag.getX())
                            .y(tag.getY())
                            .build();
                    imgTagList.add(imgTag);
                }
            }
            BoardRes.ImgList imgInfo = new BoardRes.ImgList(img.getId(),img.getSequence(),img.getImgUrl(),imgTagList);
            imgList.add(imgInfo);
        }

        return BoardConvertor.BoardDetail(board,imgList,userId);
    }


    public void setBoard(Long userId, List<String> imgPaths,BoardReq.PostBoard postBoard) {
        Board board = Board.builder()
                .userId(userId)
                .title(postBoard.getTitle())
                .content(postBoard.getContent())
                .latitude(postBoard.getLatitude())
                .longitude(postBoard.getLongitude())
                .build();
        Long boardId = boardRepository.save(board).getId();

        List<String> imgList = new ArrayList<>();

        int sequence = 1;
        for (int i=0;i<postBoard.getImgList().size();i++) {
//            String imgUrl = s3Service.upload(postBoard.getImgList().get(i).getMultipartFile());
            BoardImg boardImg = new BoardImg(imgPaths.get(i),boardId,sequence);
            sequence++;
            Long boardImgId = boardImgRepository.save(boardImg).getId();
//            if(postBoard.getImgList().get(i).getImgId() != null){
            BoardImgTag boardImgTag = BoardImgTag.builder()
                    .imgId(boardImgId)
                    .brandId(postBoard.getImgList().get(i).getBrandId())
                    .x(postBoard.getImgList().get(i).getX())
                    .y(postBoard.getImgList().get(i).getY())
                    .build();
            boardImgTagRepository.save(boardImgTag);
//            }
            imgList.add(boardImg.getImgUrl());
        }

        LocalDateTime now = LocalDateTime.now();
        Long longTime = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("DB에 저장 후 : " + longTime);

//        for(String tag : postBoard.getTags()){
//            BoardHashTag boardHashTag = BoardHashTag.builder().boardId(boardId).tag(tag).build();
//
//            boardHashTagRepository.save(boardHashTag);
//        }




    }

    public void updateBoard(Long userId, List<String> imgPaths, BoardReq.UpdateBoard updateBoard) {
        Board board = boardRepository.getOne(updateBoard.getBoardId());
        board.updateBoard(updateBoard.getTitle(),updateBoard.getContent(),updateBoard.getLatitude(),updateBoard.getLongitude());
        boardRepository.save(board);

        List<String> imgList = new ArrayList<>();
        int sequence = boardImgRepository.findImgSequenceByBoardId(updateBoard.getBoardId());
        System.out.println("마지막 시퀀스 : " + sequence);
        for(int i=0;i<updateBoard.getImgList().size();i++){
            BoardImg boardImg = new BoardImg(imgPaths.get(i),updateBoard.getBoardId(),sequence);
            sequence++;
            Long boardImgId = boardImgRepository.save(boardImg).getId();
            if(updateBoard.getImgList().get(i).getImgId() != null){
                BoardImgTag boardImgTag = BoardImgTag.builder()
                        .imgId(boardImgId)
                        .brandId(updateBoard.getImgList().get(i).getBrandId())
                        .x(updateBoard.getImgList().get(i).getX())
                        .y(updateBoard.getImgList().get(i).getY())
                        .build();
                boardImgTagRepository.save(boardImgTag);

            }
            imgList.add(boardImg.getImgUrl());

        }
    }

    public void fileDelete(Long deleteImgUrlId) {
        String deleteImgUrl = boardImgRepository.findImgUrlByboardImgId(deleteImgUrlId);
        s3Service.fileDelete(deleteImgUrl);
        boardImgRepository.deleteById(deleteImgUrlId);
    }
  
    public boolean existsBoardByBoardId(Long boardId) {
        return boardRepository.existsById(boardId);
    }

    public boolean existsLike(Long userId, Long boardId) {
        return boardLikeRepository.existsByUserIdAndBoardId(userId, boardId); //
    }

    public void likeBoard(Long userId, Long boardId) {
        BoardLike boardLike = BoardLike.builder().id(new BoardLikePk(userId,boardId)).build();
        boardLikeRepository.save(boardLike);
    }

    public void deleteLike(Long userId, Long boardId) {
        boardLikeRepository.deleteByUserIdAndBoardId(userId, boardId);
    }

    public List<String> getTagList(String content) {
        List<BoardHashTag> boardHashTags = boardHashTagRepository.findByTagContainingOrderByCreatedAtAsc(content);

        return boardHashTags.stream().map(BoardHashTag::getTag).collect(Collectors.toList());
    }

    public void deleteBoard(Long boardId) {
        List<Long> deleteImgIdList = boardImgRepository.findImgIdByBoardId(boardId);
        for(Long deleteImgId : deleteImgIdList){
            fileDelete(deleteImgId);
        }
        boardRepository.deleteById(boardId);
    }

    public void saveBoard(Long userId, List<String> imgPaths, BoardReq.SaveBoardV2 postBoard, JSONArray jsonArray) {
        Board board = Board.builder()
                .userId(userId)
                .title(postBoard.getTitle())
                .content(postBoard.getContent())
                .latitude(postBoard.getLatitude())
                .longitude(postBoard.getLongitude())
                .build();

        Long boardId = boardRepository.save(board).getId();


        int sequence = 1;

        for (int i = 0; i < jsonArray.size(); i++) {
            BoardImg boardImg = new BoardImg(imgPaths.get(i),boardId,sequence);
            sequence++;
            Long boardImgId = boardImgRepository.save(boardImg).getId();

            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            JSONArray imgTagsArray = (JSONArray) jsonObject.get("imgTags");

            System.out.println(i);

            for (Object o : imgTagsArray) {
                JSONObject imgTagsObject = (JSONObject) o;

                Object brandId = imgTagsObject.get("brandId");
                double x;
                double y;


                Object xObject = imgTagsObject.get("x");
                if (xObject instanceof Double) {
                    x = (double) xObject;
                } else if (xObject instanceof Integer) {
                    x = ((Integer) xObject).doubleValue();
                } else {
                    // Handle the case when the value is not a valid number
                    // For example, throw an exception or set a default value
                    x = 0.0;
                }

                Object yObject = imgTagsObject.get("y");
                if (yObject instanceof Double) {
                    y = (double) yObject;
                } else if (yObject instanceof Integer) {
                    y = ((Integer) yObject).doubleValue();
                } else {
                    // Handle the case when the value is not a valid number
                    // For example, throw an exception or set a default value
                    y = 0.0;
                }

                BoardImgTag boardImgTag = BoardImgTag.builder()
                        .imgId(boardImgId)
                        .brandId((Long) brandId)
                        .x(x)
                        .y(y)
                        .build();


                boardImgTagRepository.save(boardImgTag);
            }
        }


        for(String tag : postBoard.getTags()){
            BoardHashTag boardHashTag = BoardHashTag.builder().boardId(boardId).tag(tag).build();

            boardHashTagRepository.save(boardHashTag);
        }






    }

}
