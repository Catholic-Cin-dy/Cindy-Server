package com.app.cindy.service;

import com.app.cindy.convertor.ProductConverter;
import com.app.cindy.domain.product.Product;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.exception.BaseException;
import com.app.cindy.repository.ProductLikeRepository;
import com.app.cindy.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.app.cindy.constants.CommonResponseStatus.PRODUCT_NOT_FOUND;
import static com.app.cindy.constants.CommonResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    public PageResponse<List<ProductRes.ProductList>> getProductList(int filter, Integer page, Integer size,Long userId) {
        Pageable pageReq = PageRequest.of(page, size);
        List<ProductRes.ProductList> productList=new ArrayList<>();
        Page<ProductRepository.GetProductList> product = null;
        if(filter==0){
            product=productRepository.findAllFetchJoin(userId,pageReq);
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
        return null;
    }

    public ProductRes.ProductDetail getProductDetail(Long id, Long productId) {
        ProductRepository.GetProductDetail product = productRepository.findByIdAndStatus(productId,id).orElseThrow(() ->
                new BadRequestException(PRODUCT_NOT_FOUND));

        ProductRes.ProductDetail productDetail = ProductConverter.convertToProductDetail(product);

        return productDetail;
    }
}
