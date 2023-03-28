package com.app.cindy.convertor;

import com.app.cindy.domain.product.Product;
import com.app.cindy.domain.product.ProductImg;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.repository.ProductRepository;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductConverter {


    public static ProductRes.ProductDetail convertToProductDetail(ProductRepository.GetProductDetail product) {
        return ProductRes.ProductDetail
                .builder()
                .productId(product.getProductId())
                .productImg(Stream.of(product.getImgUrl().split(",")).collect(Collectors.toList()))
                .brandName(product.getBrandName())
                .bookmark(product.getBookMark())
                .productName(product.getName())
                .build();
    }
}
