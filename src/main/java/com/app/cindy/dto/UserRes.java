package com.app.cindy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-01 유저 로그인 🔑 API Response")
    public static class Token {
        @ApiModelProperty(notes = "user 인덱스", required = true, example = "1")
        private Long userId; //user 인덱스
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ-----")
        private String accessToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class GenerateToken{
        private String accessToken;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-02 회원가입 👤 API Response")
    public static class SignUp {
        @ApiModelProperty(notes = "user 인덱스", required = true, example = "1")
        private Long userId; //user 인덱스
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ-----")
        private String accessToken;
        @Schema(description = "리프레쉬 토큰", required = true, example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ----")
        private String refreshToken;
        @ApiModelProperty(notes = "프로필 이미지",required = false,example = "이미지 url")
        private String imgUrl;
        @ApiModelProperty(notes = "유저 닉네임",required = true,example = "이미지 url")
        private String nickname;
        @ApiModelProperty(notes = "카테고리 리스트",required = true,example = "카테고리 리스트")
        private List<String> categoryList;

    }
}
