package com.app.cindy.repository;

import com.app.cindy.domain.board.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select C.id'commentId',U.id'userId',profile_url'profileImgUrl',nickname,C.content'comment'"+
            "    ,case\n" +
            "                  when YEAR(C.created_at) < YEAR(now())\n" +
            "                       then concat(YEAR(C.created_at), '년 ', MONTH(C.created_at), '월 ', DAY(C.created_at), '일')\n" +
            "                   when YEAR(C.created_at) = YEAR(now()) then\n" +
            "                       case\n" +
            "                           when (TIMESTAMPDIFF(DAY, C.created_at, now())) > 7\n" +
            "                               then concat(month(C.created_at), '월 ', DAY(C.created_at), '일')\n" +
            "                           when TIMESTAMPDIFF(minute, C.created_at, now()) < 1\n" +
            "                               then concat(TIMESTAMPDIFF(second, C.created_at, now()),'초 전')\n" +
            "                          when TIMESTAMPDIFF(hour, C.created_at, now()) > 24\n" +
            "                               then concat(TIMESTAMPDIFF(DAY, C.created_at, now()), '일 전')\n" +
            "                           when TIMESTAMPDIFF(hour, C.created_at, now()) < 1\n" +
            "                               then concat(TIMESTAMPDIFF(minute, C.created_at, now()), '분 전')\n" +
            "                           when TIMESTAMPDIFF(hour, C.created_at, now()) < 24\n" +
            "                              then concat(TIMESTAMPDIFF(hour, C.created_at, now()), '시간 전')\n" +
            "                          end end as                                          commentTime\n" +
            "from Comment C " +
            "join User U on C.user_id=U.id where C.board_id=:boardId",nativeQuery = true,countQuery = "select count(*) from Comment where board_id=:boardId")
    Page<CommentRepository.BoardComment> getBoardComments(@Param("boardId") Long boardId, Pageable pageReq);

    @Query(value = "select C.board_id'boardId', C.id'commentId',U.id'userId',profile_url'profileImgUrl',nickname,C.content'comment'"+
            "    ,case\n" +
            "                  when YEAR(C.created_at) < YEAR(now())\n" +
            "                       then concat(YEAR(C.created_at), '년 ', MONTH(C.created_at), '월 ', DAY(C.created_at), '일')\n" +
            "                   when YEAR(C.created_at) = YEAR(now()) then\n" +
            "                       case\n" +
            "                           when (TIMESTAMPDIFF(DAY, C.created_at, now())) > 7\n" +
            "                               then concat(month(C.created_at), '월 ', DAY(C.created_at), '일')\n" +
            "                           when TIMESTAMPDIFF(minute, C.created_at, now()) < 1\n" +
            "                               then concat(TIMESTAMPDIFF(second, C.created_at, now()),'초 전')\n" +
            "                          when TIMESTAMPDIFF(hour, C.created_at, now()) > 24\n" +
            "                               then concat(TIMESTAMPDIFF(DAY, C.created_at, now()), '일 전')\n" +
            "                           when TIMESTAMPDIFF(hour, C.created_at, now()) < 1\n" +
            "                               then concat(TIMESTAMPDIFF(minute, C.created_at, now()), '분 전')\n" +
            "                           when TIMESTAMPDIFF(hour, C.created_at, now()) < 24\n" +
            "                              then concat(TIMESTAMPDIFF(hour, C.created_at, now()), '시간 전')\n" +
            "                          end end as                                          commentTime\n" +
            "from Comment C " +
            "join User U on C.user_id=U.id where C.board_id=:boardId",nativeQuery = true,countQuery = "select count(*) from Comment where board_id=:boardId")
    Page<CommentRepository.MyBoardsComments> getMyBoardsComments(@Param("boardId") Long boardId, Pageable pageReq);

    interface BoardComment {
        Long getCommentId();
        Long getUserId();
        String getProfileImgUrl();
        String getNickName();
        String getComment();
        String getCommentTime();
    }

    interface MyBoardsComments {
        Long getBoardId();
        Long getCommentId();
        Long getUserId();
        String getProfileImgUrl();
        String getNickName();
        String getComment();
        String getCommentTime();
    }
}
