package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "04-ootd 게시판 👗")
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
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
    @ApiOperation(value = "04-03 ootd 게시판 상세 조회 댓글 조회 👗 API #FRAME OOTD 02", notes = "게시판 상세 조회 API 입니다. 04-03 댓글 상세조회와 함께 세트입니당")
    public CommonResponse<PageResponse<List<BoardRes.BoardComment>>> getBoardComments(@AuthenticationPrincipal User user,
                                                                                      @Parameter(description ="boardId 값 보내주세요",example = "1") @PathVariable("boardId") Long boardId,
                                                                                      @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                                      @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size){
        Long userId= user.getId();
        PageResponse<List<BoardRes.BoardComment>> boardComment = boardService.getBoardComments(userId,boardId,page,size);

        return  CommonResponse.onSuccess(boardComment);
    }


}
