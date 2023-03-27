package com.app.cindy.repository;

import com.app.cindy.domain.product.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    boolean existsByProductIdAndUserId(Long id, Long userId);
}
