package com.app.cindy.service;

import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

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
                                result.getBoardTime(),
                                result.getLikeCheck()
                        )
                )
        );

        return new PageResponse<>(board.isLast(),boardList);
    }
}
