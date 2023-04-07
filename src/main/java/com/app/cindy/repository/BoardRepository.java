package com.app.cindy.repository;

import com.app.cindy.domain.board.Board;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {


    @Query(value = "select B.id'boardId', B.title'title',U.nickname'writer', GROUP_CONCAT(BI.img_url)'boardImg'\n" +
            "     ,(select count(*) from BoardLike BL where BL.board_id=B.id) as likeCnt , (select count(*) from Comment C where C.board_id=B.id) as commentCnt\n" +
            "    ,case\n" +
            "                  when YEAR(B.created_at) < YEAR(now())\n" +
            "                       then concat(YEAR(B.created_at), '년 ', MONTH(B.created_at), '월 ', DAY(B.created_at), '일')\n" +
            "                   when YEAR(B.created_at) = YEAR(now()) then\n" +
            "                       case\n" +
            "                           when (TIMESTAMPDIFF(DAY, B.created_at, now())) > 7\n" +
            "                               then concat(month(B.created_at), '월 ', DAY(B.created_at), '일')\n" +
            "                           when TIMESTAMPDIFF(minute, B.created_at, now()) < 1\n" +
            "                               then concat(TIMESTAMPDIFF(second, B.created_at, now()),'초 전')\n" +
            "                          when TIMESTAMPDIFF(hour, B.created_at, now()) > 24\n" +
            "                               then concat(TIMESTAMPDIFF(DAY, B.created_at, now()), '일 전')\n" +
            "                           when TIMESTAMPDIFF(hour, B.created_at, now()) < 1\n" +
            "                               then concat(TIMESTAMPDIFF(minute, B.created_at, now()), '분 전')\n" +
            "                           when TIMESTAMPDIFF(hour, B.created_at, now()) < 24\n" +
            "                              then concat(TIMESTAMPDIFF(hour, B.created_at, now()), '시간 전')\n" +
            "                          end end as                                          boardTime\n" +
            "from Board B\n" +
            "join User U on B.user_id = U.id\n" +
            "join BoardImg BI on B.id = BI.board_id\n" +
            "group by B.id\n" +
            "order by likeCnt\n" +
            "",nativeQuery = true,countQuery = "select count(*) from Board B ")
    Page<GetBoardList> findAllBoardByLike(@Param("userId") Long userId, Pageable pageReq);

    interface GetBoardList{
        Long getBoardId();
        String getTitle();
        String getWriter();
        String getBoardImg();
        Long getLikeCnt();
        Long getCommentCnt();
        String getBoardTime();
    }
}
