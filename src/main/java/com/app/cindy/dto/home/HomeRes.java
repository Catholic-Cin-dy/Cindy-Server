package com.app.cindy.dto.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

public class HomeRes {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-01 홈 화면 배너 조회 🏠")
    public static class HomeBanner{

        @ApiModelProperty(notes ="타이틀", required = true, example = "홍보 또는 사진과 관련된 문구")
        private String title;

        @ApiModelProperty(notes ="내용", required = true, example = "문구와 관련된 부제목")
        private String content;

        @ApiModelProperty(notes ="배너 url", required = true, example = "배너 url")
        private String bannerUrl;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "02-03 신상 상품 전체 조회 🏠")
    public static class HomeNewProduct{
        @ApiModelProperty(notes ="브랜드", required = true, example = "프라다")
        private String brand;

        @ApiModelProperty(notes ="상품명", required = true, example = "스몰 나파 가죽")
        private String productName;

        @ApiModelProperty(notes ="상품 이미지 url", required = true, example = "상품 이미지 url")
        private String productImgUrl;
    }
}
