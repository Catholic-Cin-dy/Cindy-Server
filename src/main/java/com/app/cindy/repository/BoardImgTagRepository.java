package com.app.cindy.repository;

import com.app.cindy.domain.board.BoardImgTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardImgTagRepository extends JpaRepository<BoardImgTag,Long> {
    @Query("select b from BoardImgTag b join fetch b.brand where b.imgId=:id")
    List<BoardImgTag> findByImgId(Long id);
}
