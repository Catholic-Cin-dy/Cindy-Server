package com.app.cindy.convertor;

import com.app.cindy.domain.board.Comment;
import com.app.cindy.dto.board.BoardReq;
import com.app.cindy.dto.board.BoardRes;
import com.app.cindy.repository.BoardRepository;

import java.util.List;

public class BoardConvertor {
    public static BoardRes.BoardDetail BoardDetail(BoardRepository.GetBoardDetail board, List<BoardRes.ImgList> imgList, Long userId) {
        return BoardRes.BoardDetail.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .profileImgUrl(board.getImgUrl())
                .writer(board.getWriter())
                .imgList(imgList)
                .likeCount(board.getLikeCnt())
                .likeCheck(board.getLikeCheck())
                .commentCount(board.getCommentCnt())
                .boardTime(board.getBoardTime())
                .isMy(board.getUserId().equals(userId))
                .build();
    }

    public static Comment PostComment(Long userId, BoardReq.Comment comment) {
        return Comment.builder().userId(userId).boardId(comment.getBoardId()).content(comment.getComment()).build();
    }
}
