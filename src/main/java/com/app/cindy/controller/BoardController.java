package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardReq;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.exception.BaseException;
import com.app.cindy.service.BoardService;
import com.app.cindy.service.CommentService;
import com.app.cindy.service.S3Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;

import static com.app.cindy.constants.CommonResponseStatus.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "04-ootd 게시판 👗")
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final S3Service s3Service;
    private final CommentService commentService;

    @PostMapping("")
    @ApiOperation(value = "04-01 ootd 게시판 조회 👗 API #FRAME OOTD 01", notes = "")
    public CommonResponse<PageResponse<List<BoardRes.BoardList>>> getBoardList(@AuthenticationPrincipal User user,
                                                                               @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                               @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size,
                                                                               @RequestParam(value = "정렬",required = false,defaultValue="0") Integer sort,
                                                                               @RequestBody(required = false) UserReq.Distance distance){
        PageResponse<List<BoardRes.BoardList>> boardList = boardService.getBoardList(sort,page,size,user.getId(),distance);
        System.out.println(boardList);

        return  CommonResponse.onSuccess(boardList);
    }

    @GetMapping("/{boardId}")
    @ApiOperation(value = "04-02 ootd 게시판 상세 조회 👗 API #FRAME OOTD 02", notes = "게시판 상세 조회 API 입니다. 04-03 댓글 상세조회와 함께 세트입니당")
    public CommonResponse<BoardRes.BoardDetail> getBoardDetail(@AuthenticationPrincipal User user,
                                                               @Parameter(description ="boardId 값 보내주세요",example = "1") @PathVariable("boardId") Long boardId){
        Long userId= user.getId();
        BoardRes.BoardDetail boardDetail = boardService.getBoardDetail(userId,boardId);

        return  CommonResponse.onSuccess(boardDetail);
    }

    @GetMapping("/comments/{boardId}")
    @ApiOperation(value = "04-06 ootd 게시판 상세 조회 댓글 조회 👗 API #FRAME OOTD 02", notes = "게시판 상세 조회 API 입니다. 04-03 댓글 상세조회와 함께 세트입니당")
    public CommonResponse<PageResponse<List<BoardRes.BoardComment>>> getBoardComments(@AuthenticationPrincipal User user,
                                                                                      @Parameter(description ="boardId 값 보내주세요",example = "1") @PathVariable("boardId") Long boardId,
                                                                                      @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                                      @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size){
        Long userId= user.getId();

        if(!boardService.existsBoardByBoardId(boardId))throw new BadRequestException(NOT_EXIST_BOARD);

        PageResponse<List<BoardRes.BoardComment>> boardComment = commentService.getBoardComments(userId,boardId,page,size);

        return  CommonResponse.onSuccess(boardComment);
    }

    @PostMapping(value = "/new" ,consumes = {"multipart/form-data"})
    @ApiOperation(value = "04-04 ootd 게시판 작성 👗 API #FRAME OOTD 03", notes = "")
    public CommonResponse<String> setBoard(@AuthenticationPrincipal User user,
                                           @RequestPart("postBoard") BoardReq.PostBoard postBoard,
                                           @RequestPart("imgUrl") List<MultipartFile> multipartFiles) throws BaseException, IOException {
        Long userId = user.getId();

        if (postBoard.getTitle() == null) {
            throw new BadRequestException(BOARD_NOT_WRITE_TITLE);
        }
        if (postBoard.getContent() == null) {
            throw new BadRequestException(BOARD_NOT_WRITE_CONTENT);
        }
        if (multipartFiles.get(0) == null) {
            throw new BadRequestException(BOARD_NOT_UPLOAD_IMG);
        }
        List<String> imgPaths = s3Service.upload(multipartFiles);
        System.out.println("IMG 경로들 : " + imgPaths);
        boardService.setBoard(userId, imgPaths, postBoard);
        return CommonResponse.onSuccess("생성 완료.");
    }

    @PostMapping("/comments")
    @ApiOperation(value = "04-07 ootd 게시판 댓글 달기 👗", notes = "")
    public CommonResponse<String> postComment(@AuthenticationPrincipal User user,@RequestBody BoardReq.Comment comment){
        if(!boardService.existsBoardByBoardId(comment.getBoardId()))throw new BadRequestException(NOT_EXIST_BOARD);
        Long userId = user.getId();
        commentService.postComment(userId,comment);
        return CommonResponse.onSuccess("댓글 작성 완료");
    }

    @PatchMapping("/like/{boardId}")
    @ApiOperation(value = "04-10 ootd 게시판 좋아요 👗", notes = "")
    public CommonResponse<String> likeBoard(@AuthenticationPrincipal User user,@Parameter(description ="boardId 값 보내주세요",example = "1") @PathVariable("boardId") Long boardId){
        Long userId = user.getId();
        if(!boardService.existsBoardByBoardId(boardId)) throw new BadRequestException(NOT_EXIST_BOARD);
        boolean checkLike=boardService.existsLike(userId,boardId);
        String result="";
        if(checkLike){
            boardService.deleteLike(userId,boardId);
            result="좋아요 취소 성공";
        }else{
            boardService.likeBoard(userId,boardId);
            result="좋아요 성공";
        }

        return CommonResponse.onSuccess(result);
    }

    @GetMapping("/tag")
    @ApiOperation(value = "04-11 ootd 게시판 작성 시 태그 검색 조회 👗", notes = "")
    public CommonResponse<List<String>> getTagList(@Param("content") String content) {
        List<String> tagList = boardService.getTagList(content);

        return CommonResponse.onSuccess(tagList);
    }

    @PatchMapping(value = "/update/{userId}/{boardId}" ,consumes = {"multipart/form-data"})
    @ApiOperation(value = "04-03 ootd 게시판 수정 👗 API #FRAME OOTD 04", notes = "")
    public CommonResponse<String> setBoardUpdate(@AuthenticationPrincipal User user,
                                           @RequestPart("postBoard") BoardReq.PostBoard postBoard,
                                           @RequestPart("imgUrl") List<MultipartFile> multipartFiles) throws BaseException, IOException {
        Long userId = user.getId();

        if (postBoard.getTitle() == null) {
            throw new BadRequestException(BOARD_NOT_WRITE_TITLE);
        }
        if (postBoard.getContent() == null) {
            throw new BadRequestException(BOARD_NOT_WRITE_CONTENT);
        }
        if (multipartFiles.get(0) == null) {
            throw new BadRequestException(BOARD_NOT_UPLOAD_IMG);
        }
        List<String> imgPaths = s3Service.upload(multipartFiles);
        System.out.println("IMG 경로들 : " + imgPaths);
        boardService.setBoard(userId, imgPaths, postBoard);
        return CommonResponse.onSuccess("수정 완료.");
    }


}
