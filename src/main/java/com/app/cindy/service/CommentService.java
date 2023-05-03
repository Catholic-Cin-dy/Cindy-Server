package com.app.cindy.service;

import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

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
}
