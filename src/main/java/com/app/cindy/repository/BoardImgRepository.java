package com.app.cindy.repository;

import com.app.cindy.domain.board.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
    List<BoardImg> findByBoardId(Long boardId);
}
