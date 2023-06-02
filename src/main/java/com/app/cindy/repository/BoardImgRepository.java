package com.app.cindy.repository;

import com.app.cindy.domain.board.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
    @Query("select bi from BoardImg bi join fetch bi.board b join fetch b.user where bi.boardId=:boardId")
    List<BoardImg> findByBoardId(Long boardId);

    @Query(value = "select BI.img_url'imgurl' from BoardImg BI where BI.id=:boardImgId" ,nativeQuery = true)
    String findImgUrlByboardImgId(Long boardImgId);

    @Query(value = "select BI.sequence'sequence' from BoardImg BI where board_id=:boardId order by BI.sequence desc limit 1", nativeQuery = true)
    int findImgSequenceByBoardId(Long boardId);

    @Query(value = "select BI.id'id' from BoardImg BI where board_id=:boardId", nativeQuery = true)
    List<Long> findImgIdByBoardId(Long boardId);
}
