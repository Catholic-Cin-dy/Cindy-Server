package com.app.cindy.repository;

import com.app.cindy.domain.board.BoardHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardHashTagRepository extends JpaRepository<BoardHashTag, Long> {
    List<BoardHashTag> findByTagContainingOrderByCreatedAtAsc(String content);
}
