package com.app.cindy.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UserReq {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUserInfo {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupUser {
        private String username;

        private String password;

        private String nickname;

        private String name;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-01 소셜 로그인 토큰 API Request🔑")
    public static class SocialLogin {
        @ApiModelProperty(notes ="액세스 토큰", required = true, example = "소셜 액세스 토큰")
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-02 소셜 회원가입 Request🔑")
    public static class SocialSignUp {
        @ApiModelProperty(notes = "소셜 id", required = true, example = "214124215125")
        private String socialId;

        @ApiModelProperty(notes = "소셜 프로필 사진", required = true, example = "이미지 url")
        private String profileImgUrl;

        @ApiModelProperty(notes = "카테고리 리스트 ArrayList<Long> 형식입니다. 취향", required = true, example = "[1,2,3,4]")
        private List<Long> categoryList;

        @ApiModelProperty(notes = "성별", required = true, example = "male")
        private String gender;

        @ApiModelProperty(notes = "실명", required = true, example = "전민기")
        private String name;

        @ApiModelProperty(notes ="비밀번호", required = true, example = "*********")
        private String password;

        @ApiModelProperty(notes = "닉네임", required = true, example = "타일러")
        private String nickname;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-01 유저 좌표 받기")
    public static class Distance{
        @ApiModelProperty(notes = "위도",required = false,example = "37.541")
        private double latitude;

        @ApiModelProperty(notes = "경도",required = false,example = "126.986")
        private double longitude;
    }
}
