package com.app.cindy.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

public class ProductRes {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-03 상품 상세 조회 🏬 API Response #FRAME PRODUCT 02")
    public static class ProductDetail{
        @ApiModelProperty(notes ="상품 ID", required = true, example = "1")
        private Long productId;
        @ApiModelProperty(notes ="상품 이미지 리스트", required = true, example = "프라다")
        private List<String> productImg;
        @ApiModelProperty(notes ="브랜드 이름", required = true, example = "프라다")
        private String brandName;
        @ApiModelProperty(notes ="상품 이름", required = true, example = "프라다")
        private String productName;
        @ApiModelProperty(notes ="북마크 유무", required = true, example = "true")
        private boolean bookmark;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "03-01 상품 리스트 조회 🏬 API Response #FRAME PRODUCT 01")
    public static class ProductList{
        @ApiModelProperty(notes ="상품 ID", required = true, example = "1")
        private Long productId;
        @ApiModelProperty(notes ="브랜드 이름", required = true, example = "프라다")
        private String brandName;
        @ApiModelProperty(notes ="상품 이름", required = true, example = "프라다")
        private String productName;
        @ApiModelProperty(notes ="이미지 url", required = true, example = "이미지 url")
        private String imgUrl;
        @ApiModelProperty(notes ="북마크 유무", required = true, example = "true")
        private boolean bookmark;


    }
}
