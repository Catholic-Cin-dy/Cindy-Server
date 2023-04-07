package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "04-ootd 게시판 👗")
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    @ApiOperation(value = "04-01 ootd 게시판 조회 👗 API Response #FRAME PRODUCT 01", notes = "")
    public CommonResponse<PageResponse<List<BoardRes.BoardList>>> getBoardList(@AuthenticationPrincipal User user,
                                                                               @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                               @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size,
                                                                               @RequestParam(value = "정렬",required = false,defaultValue="0") Integer sort){
        PageResponse<List<BoardRes.BoardList>> boardList = boardService.getBoardList(sort,page,size,user.getId());
        System.out.println(boardList);

        return  CommonResponse.onSuccess(boardList);
    }
}
