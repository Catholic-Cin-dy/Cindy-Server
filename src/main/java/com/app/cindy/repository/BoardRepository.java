package com.app.cindy.repository;

import com.app.cindy.domain.board.Board;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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
            "                          end end as                                          boardTime,\n" +
            " IF((select exists(select * from BoardLike BL where BL.user_id=:userId and BL.board_id=B.id)),'true','false') as likeCheck\n" +
            "from Board B\n" +
            "join User U on B.user_id = U.id\n" +
            "join BoardImg BI on B.id = BI.board_id\n" +
            "group by B.id\n" +
            "order by likeCnt desc\n" +
            "",nativeQuery = true,countQuery = "select count(*) from Board B ")
    Page<GetBoardList> findAllBoardByLike(@Param("userId") Long userId, Pageable pageReq);

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
            "                          end end as                                          boardTime,\n" +
            " IF((select exists(select * from BoardLike BL where BL.user_id=:userId and BL.board_id=B.id)),'true','false') as likeCheck\n" +
            "from Board B\n" +
            "join User U on B.user_id = U.id\n" +
            "join BoardImg BI on B.id = BI.board_id\n" +
            "group by B.id\n" +
            "order by B.created_at desc\n",nativeQuery = true,countQuery = "select count(*) from Board B ")
    Page<GetBoardList> findAllBoardByCreate(@Param("userId") Long userId, Pageable pageReq);

    @Query(value = "select B.id'boardId', B.title'title',U.nickname'writer', GROUP_CONCAT(BI.img_url)'boardImg', U.profile_url'profileImg'\n" +
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
            "                          end end as                                          boardTime,\n" +
            " IF((select exists(select * from BoardLike BL where BL.user_id=:userId and BL.board_id=B.id)),'true','false') as likeCheck,\n" +
            "(6371*acos(cos(radians(:latitude))*cos(radians(B.latitude))*cos(radians(B.longitude)   -radians(:longitude))+sin(radians(:latitude))*sin(radians(B.latitude))))as distance\n" +
            "from Board B\n" +
            "join User U on B.user_id = U.id\n" +
            "join BoardImg BI on B.id = BI.board_id\n" +
            "group by B.id\n" +
            "order by distance\n" +
            "",nativeQuery = true,countQuery = "select count(*) from Board B ")
    Page<GetBoardList> findAllBoardByDistance(@Param("userId") Long userId, Pageable pageReq,@Param("latitude") double latitude,@Param("longitude") double longitude);

    @Query(value = "select B.user_id'userId',B.id'boardId', B.title'title',U.nickname'writer', U.profile_url'imgUrl',GROUP_CONCAT(BI.img_url)'boardImg'\n" +
            "     ,(select count(*) from BoardLike BL where BL.board_id=B.id) as likeCnt, content , (select count(*) from Comment C where C.board_id=B.id) as commentCnt\n" +
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
            "                          end end as                                          boardTime," +
            " IF((select exists(select * from BoardLike BL where BL.user_id=:userId and BL.board_id=:boardId)),'true','false')'likeCheck'\n" +
            "from Board B \n" +
            "join User U on B.user_id = U.id\n" +
            "join BoardImg BI on B.id = BI.board_id where B.id=:boardId",nativeQuery = true)
    BoardRepository.GetBoardDetail getBoardDetail(@Param("userId") Long userId,@Param("boardId") Long boardId);


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
            "                          end end as                                          boardTime,\n" +
            " IF((select exists(select * from BoardLike BL where BL.user_id=:userId and BL.board_id=B.id)),'true','false') as likeCheck\n" +
            "from Board B\n" +
            "join User U on B.user_id = U.id\n" +
            "join BoardImg BI on B.id = BI.board_id\n" +
            "where B.user_id = :userId\n" +
            "group by B.id\n" +
            "order by B.created_at desc\n",nativeQuery = true,countQuery = "select count(*) from Board B ")
    Page<GetBoardList> findAllMyBoard(@Param("userId") Long userId, Pageable pageReq);


    List<GetBoardIdList> findByUserId(Long userId);

    Optional<Board> findById(Long boardId);

    @Query(value = "select B.view'view' from Board B where B.id = :boardId",nativeQuery = true)
    Long getView(Long boardId);

    interface GetBoardIdList{
        Long getId();
    }

    interface GetBoardList{

        Long getBoardId();
        String getTitle();
        String getWriter();
        String getBoardImg();
        String getProfileImg();
        Long getLikeCnt();
        Long getCommentCnt();
        String getBoardTime();
        boolean getLikeCheck();
    }

    interface GetBoardDetail {
        Long getUserId();
        Long getBoardId();
        String getTitle();
        String getContent();
        String getImgUrl();
        String getWriter();
        Long getLikeCnt();
        Long getCommentCnt();
        boolean getLikeCheck();
        String getBoardTime();
    }
}
