package com.app.cindy.service;

import com.app.cindy.convertor.ProductConverter;
import com.app.cindy.domain.pk.ProductLikePk;
import com.app.cindy.domain.product.Product;
import com.app.cindy.domain.product.ProductLike;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.exception.ForbiddenException;
import com.app.cindy.repository.ProductLikeRepository;
import com.app.cindy.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.app.cindy.constants.CommonResponseStatus.DATABASE_ERROR;
import static com.app.cindy.constants.CommonResponseStatus.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final RedisService redisService;


    public PageResponse<List<ProductRes.ProductList>> getProductList(int filter, Integer page, Integer size,Long userId) {
        Pageable pageReq = PageRequest.of(page, size);
        List<ProductRes.ProductList> productList=new ArrayList<>();
        Page<ProductRepository.GetProductList> product = null;
        if(filter==0){
            product=productRepository.findAllCategory(userId,pageReq);
        }else{
            product=productRepository.findByCategoryId(userId,(long) filter,pageReq);
        }


        product.forEach(
                result -> productList.add(
                        new ProductRes.ProductList(
                                result.getProductId(),
                                result.getBrandName(),
                                result.getName(),
                                result.getImgUrl(),
                                result.getBookMark()
                ))
        );


        return new PageResponse<>(product.isLast(),productList);
    }

    public PageResponse<List<ProductRes.ProductList>> getProductListByContent(String content, Integer page, Integer size, Long id) {
        Pageable pageReq = PageRequest.of(page, size);

        List<ProductRes.ProductList> productList=new ArrayList<>();

        Page<ProductRepository.GetProductList> searchList = productRepository.GetProductListByContent(id,content,pageReq);

        searchList.forEach(
                result -> productList.add(
                        new ProductRes.ProductList(
                                result.getProductId(),
                                result.getBrandName(),
                                result.getName(),
                                result.getImgUrl(),
                                result.getBookMark()
                        )
                )
        );

        return new PageResponse<>(searchList.isLast(),productList);
    }

    public ProductRes.ProductDetail getProductDetail(Long id, Long productId) {
        ProductRepository.GetProductDetail product = productRepository.findByIdAndStatus(productId,id).orElseThrow(() ->
                new BadRequestException(PRODUCT_NOT_FOUND));

        redisService.addToViewingHistory(String.valueOf(id), String.valueOf(productId));


        return ProductConverter.convertToProductDetail(product);
    }

    public List<ProductRes.ProductList> getProductOtherList(List<Long> productIds, Long userId) {
        List<ProductRes.ProductList> productList = new ArrayList<>();

        List<ProductRepository.GetProductList> otherProduct=productRepository.getProductOtherList(productIds,userId);

        otherProduct.forEach(
                result-> productList.add(new ProductRes.ProductList(
                        result.getProductId(),
                        result.getBrandName(),
                        result.getName(),
                        result.getImgUrl(),
                        result.getBookMark()
                )
                )
        );

        return productList;

    }

    public List<ProductRes.ProductList> getProductBrandList(Long productId, Long userId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new BadRequestException(PRODUCT_NOT_FOUND));

        List<ProductRes.ProductList> productList= new ArrayList<>();

        List<ProductRepository.GetProductList> products = productRepository.findProductBrand(product.getBrandId(),product.getCategoryId(),userId);

        products.forEach(
                result -> productList.add(
                        new ProductRes.ProductList(
                                result.getProductId(),
                                result.getBrandName(),
                                result.getName(),
                                result.getImgUrl(),
                                result.getBookMark()
                        )
                )
        );


        return productList;
    }

    public boolean existsProductLike(Long userId, Long productId) {
        try {
            return productLikeRepository.existsByProductIdAndUserId(productId, userId);
        }catch(Exception e){
            throw new ForbiddenException(DATABASE_ERROR);
        }
    }

    public void deleteProductLike(Long userId, Long productId) {
        try{
            productLikeRepository.deleteByProductIdAndUserId(productId,userId);
        }catch(Exception e){
            throw new ForbiddenException(DATABASE_ERROR);
        }
    }

    public void postProductLike(Long userId, Long productId) {
        try{
            ProductLike productLike = ProductLike.builder().id(new ProductLikePk(userId,productId)).build();
            productLikeRepository.save(productLike);
        }catch(Exception e){
            throw new ForbiddenException(DATABASE_ERROR);
        }

    }
}
