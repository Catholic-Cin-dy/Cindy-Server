package com.app.cindy.repository;

import com.app.cindy.domain.board.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    boolean existsByUserIdAndBoardId(Long userId, Long boardId);

    @Transactional
    void deleteByUserIdAndBoardId(Long userId, Long boardId);
}
