package com.app.cindy.repository;

import com.app.cindy.domain.product.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    boolean existsByProductIdAndUserId(Long id, Long userId);

    @Transactional
    void deleteByProductIdAndUserId(Long productId, Long userId);
}
