package com.app.cindy.repository;

import com.app.cindy.domain.board.BoardImgTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImgTagRepository extends JpaRepository<BoardImgTag,Long> {
    List<BoardImgTag> findByImgId(Long id);


}
