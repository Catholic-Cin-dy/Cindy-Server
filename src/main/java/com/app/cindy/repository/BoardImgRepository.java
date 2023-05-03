package com.app.cindy.repository;

import com.app.cindy.domain.board.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
    @Query("select bi from BoardImg bi join fetch bi.board b join fetch b.user ")
    List<BoardImg> findByBoardId(Long boardId);
}
