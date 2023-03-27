package com.app.cindy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
    @ApiModel(value = "01-08,09 소셜 로그인 토큰 API Request🔑")
    public static class SocialLogin {
        @ApiModelProperty(notes ="액세스 토큰", required = true, example = "소셜 액세스 토큰")
        private String accessToken;
    }
}
