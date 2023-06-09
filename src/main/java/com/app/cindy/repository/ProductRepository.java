package com.app.cindy.repository;

import com.app.cindy.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
    Page<GetProductList> findByCategoryId(@Param("userId") Long userId,@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value=
            "select  P.id'productId',B.name'brandName',P.name,P.img_url'imgUrl',product_url'productUrl'," +
                    " IF((select exists(select * from ProductLike PL where PL.user_id=:userId and PL.product_id=P.id)),'true','false')'bookmark' " +
                    " from Product P " +
                    " join Brand B on B.id = P.brand_id" +
                    " where P.id=:productId ",nativeQuery = true)
    Optional<GetProductDetail> findByIdAndStatus(@Param("productId") Long productId,@Param("userId") Long userId);

    @Query(value=
            "select  P.id'productId',B.name'brandName',P.name,P.img_url'imgUrl'," +
                    " IF((select exists(select * from ProductLike PL where PL.user_id=:userId and PL.product_id=P.id)),'true','false')'bookmark' " +
                    " from Product P " +
                    " join Brand B on B.id = P.brand_id " +
                    " join Category C on C.id=P.category_id group by P.id " +
                    "" ,nativeQuery = true,
            countQuery = "select count(*) from Product P ")
    Page<GetProductList> findAllCategory(@Param("userId") Long userId, Pageable pageable);

    @Query(value=
            "select  P.id'productId',B.name'brandName',P.name,P.img_url'imgUrl'," +
                    " IF((select exists(select * from ProductLike PL where PL.user_id=:userId and PL.product_id=P.id)),'true','false')'bookmark' " +
                    " from Product P " +
                    " join Brand B on B.id = P.brand_id " +
                    " join Category C on C.id=P.category_id where P.id IN(:productIds) group by P.id " +
                    "" ,nativeQuery = true)
    List<GetProductList> getProductOtherList(@Param("productIds") List<Long> productIds,@Param("userId") Long userId);

    @Query(value=
            "select  P.id'productId',B.name'brandName',P.name,P.img_url'imgUrl'," +
                    " IF((select exists(select * from ProductLike PL where PL.user_id=:userId and PL.product_id=P.id)),'true','false')'bookmark' " +
                    " from Product P " +
                    " join Brand B on B.id = P.brand_id " +
                    " join Category C on C.id=P.category_id where brand_id=:brandId and category_id=:categoryId group by P.id limit 10 " +
                    "" ,nativeQuery = true)
    List<GetProductList> findProductBrand(@Param("brandId") Long brandId,@Param("categoryId") Long categoryId,@Param("userId") Long userId);

    @Query(value="select P.id'productId', B.name'brand', P.name'productName', P.img_url'productImgUrl' from Product P join Brand B on P.brand_id = B.id order by created_at desc LIMIT 30;" ,nativeQuery = true)
    List<GetNewProductList> findNewProduct();


    @Query(value="select P.id'productId', B.name'brand', P.name'productName', P.img_url'productImgUrl'\n" +
            "     , SUM(CASE WHEN P.category_id IN (:userCategoryList) THEN 1 ELSE 0 END) AS categoryScore\n" +
            "from Product P\n" +
            "join Brand B on P.brand_id = B.id\n" +
            "join User U on P.gender = U.gender\n" +
            "group by P.id\n" +
            "order by categoryScore desc LIMIT 30",nativeQuery = true)
    List<GetRecommendProductList> findRecommendProduct(List<Long> userCategoryList);

    @Query(value=
            "select  P.id'productId',B.name'brandName',P.name,P.img_url'imgUrl'," +
                    " IF((select exists(select * from ProductLike PL where PL.user_id=:userId and PL.product_id=P.id)),'true','false')'bookmark' " +
                    " from Product P " +
                    " join Brand B on B.id = P.brand_id " +
                    " join Category C on C.id=P.category_id where P.name LIKE concat('%',:content,'%') group by P.id ",nativeQuery = true,
            countQuery = "select count(*) from Product P where P.name LIKE concat('%',:content,'%') ")
    Page<GetProductList> GetProductListByContent(@Param("userId") Long userId,@Param("content") String content, Pageable pageReq);

    //List<GetProductList> getProductViewingList(Long userId, List<Long> productIds);

    interface GetProductList {
        Long getProductId();
        String getName();
        String getBrandName();
        String getImgUrl();
        boolean getBookMark();

    }

    interface GetNewProductList{
        int getProductId();
        String getBrand();
        String getProductName();
        String getProductImgUrl();
    }

    interface GetRecommendProductList{
        int getProductId();
        String getBrand();
        String getProductName();
        String getProductImgUrl();
    }

    interface GetProductDetail {
        Long getProductId();
        String getName();
        String getBrandName();
        String getImgUrl();
        String getProductUrl();
        boolean getBookMark();
    }

}
