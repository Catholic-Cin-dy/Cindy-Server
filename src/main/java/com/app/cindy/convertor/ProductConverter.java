package com.app.cindy.convertor;

import com.app.cindy.domain.product.Product;
import com.app.cindy.domain.product.ProductImg;
import com.app.cindy.dto.product.ProductRes;

import java.util.stream.Collectors;

public class ProductConverter {
    public static ProductRes.ProductDetail convertToProductDetail(Product product, boolean bookmark) {
        return ProductRes.ProductDetail
                .builder()
                .productId(product.getId())
                .productImg(product.getProductImg().stream().map(ProductImg::getImgUrl).collect(Collectors.toList()))
                .brandName(product.getBrand().getName())
                .bookmark(bookmark)
                .productName(product.getName())
                .build();
    }
}
