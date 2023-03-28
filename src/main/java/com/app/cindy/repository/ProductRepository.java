package com.app.cindy.repository;

import com.app.cindy.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value=
            "select  P.id'productId',B.name'brandName',P.name,img_url'imgUrl'," +
                    " IF((select exists(select * from ProductLike PL where PL.user_id=:userId and PL.product_id=P.id)),'true','false')'bookmark' " +
                    " from Product P " +
                    " join Brand B on B.id = P.brand_id " +
                    " join Category C on C.id=P.category_id" +
                    " where P.category_id=:categoryId group by P.id" ,nativeQuery = true,
            countQuery = "select count(*) from Product P  where P.category_id=:categoryId ")
    Page<GetProductList> findByCategoryId(@Param("categoryId") Long categoryId, @Param("userId") Long userId, Pageable pageable);

    Optional<Product> findByIdAndStatus(Long productId, boolean b);

    @Query(value=
            "select  P.id'productId',B.name'brandName',P.name,P.img_url'imgUrl'," +
                    " IF((select exists(select * from ProductLike PL where PL.user_id=:userId and PL.product_id=P.id)),'true','false')'bookmark' " +
                    " from Product P " +
                    " join Brand B on B.id = P.brand_id " +
                    " join Category C on C.id=P.category_id group by P.id" +
                    "" ,nativeQuery = true,
            countQuery = "select count(*) from Product P ")
    Page<GetProductList> findAllFetchJoin(@Param("userId") Long userId, Pageable pageable);

    interface GetProductList {
        Long getProductId();
        String getName();
        String getBrandName();
        String getImgUrl();
        boolean getBookMark();

    }
}
