package com.app.cindy.service;

import com.app.cindy.domain.board.Board;
import com.app.cindy.domain.board.BoardImg;
import com.app.cindy.domain.board.BoardImgTag;
import com.app.cindy.convertor.BoardConvertor;
import com.app.cindy.domain.board.BoardImg;
import com.app.cindy.domain.board.BoardImgTag;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardReq;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.repository.BoardImgRepository;
import com.app.cindy.repository.BoardImgTagRepository;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.repository.BoardImgRepository;
import com.app.cindy.repository.BoardImgTagRepository;
import com.app.cindy.repository.BoardRepository;
import com.app.cindy.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
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
    private final S3Service s3Service;

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

    public BoardRes.BoardDetail getBoardDetail(Long userId, Long boardId) {
        List<BoardImg> boardImg = boardImgRepository.findByBoardId(boardId);
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

    public PageResponse<List<BoardRes.BoardComment>> getBoardComments(Long userId, Long boardId, @Min(value = 0) Integer page, Integer size) {
        Pageable pageReq = PageRequest.of(page, size);
        Page<CommentRepository.BoardComment> comment= commentRepository.getBoardComments(boardId,pageReq);
        List<BoardRes.BoardComment> boardComment = new ArrayList<>();

        comment.forEach(
                result -> boardComment.add(
                        new BoardRes.BoardComment(
                                result.getCommentId(),
                                result.getUserId(),
                                result.getProfileImgUrl(),
                                result.getNickName(),
                                result.getComment(),
                                result.getCommentTime(),
                                result.getUserId().equals(userId)
                        )
                )
        );
        return new PageResponse<>(comment.isLast(),boardComment);
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
            if(postBoard.getImgList().get(i).getImgId() != null){
                BoardImgTag boardImgTag = BoardImgTag.builder()
                        .imgId(boardImgId)
                        .brandId(postBoard.getImgList().get(i).getBrandId())
                        .x(postBoard.getImgList().get(i).getX())
                        .y(postBoard.getImgList().get(i).getY())
                        .build();
                System.out.println(i);
                boardImgTagRepository.save(boardImgTag);
            }
            imgList.add(boardImg.getImgUrl());
        }




    }
}
