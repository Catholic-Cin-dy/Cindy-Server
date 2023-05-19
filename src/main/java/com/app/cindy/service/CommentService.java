package com.app.cindy.service;

import com.app.cindy.convertor.BoardConvertor;
import com.app.cindy.domain.board.Comment;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.board.BoardReq;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.app.cindy.constants.CommonResponseStatus.NOT_AUTHORIZATION_DELETE;
import static com.app.cindy.constants.CommonResponseStatus.NOT_EXIST_COMMENT;

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

    public void postComment(Long userId, BoardReq.Comment comment) {
        Comment comments = BoardConvertor.PostComment(userId,comment);
        commentRepository.save(comments);
    }

    public void modifyComment(Long userId, BoardReq.ModifyComment comment) {
        Optional<Comment> commentInfo = commentRepository.findById(comment.getCommentId());
        commentInfo.get().modifyComment(comment.getComment());

        if(commentInfo.get().getUserId()!=userId){
            throw new BadRequestException(NOT_AUTHORIZATION_DELETE);
        }

        commentRepository.save(commentInfo.get());
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void checkComment(Long id, Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()) throw new BadRequestException(NOT_EXIST_COMMENT);
        if(!comment.get().getUserId().equals(id))throw new BadRequestException(NOT_AUTHORIZATION_DELETE);
    }

    public boolean existsCommentByCommentId(Long commentId) {
        return commentRepository.existsById(commentId);
    }
}
