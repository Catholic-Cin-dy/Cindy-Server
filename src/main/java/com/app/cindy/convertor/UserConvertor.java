package com.app.cindy.convertor;

import com.app.cindy.domain.Authority;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.dto.user.UserRes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class UserConvertor {
    public static Authority PostAuthroity() {
        return Authority.builder()
                .authorityName("ROLE_USER")
                .build();
    }

    public static User SignUpUser(UserReq.SignupUser signupUser, Authority authority, String passwordEncoded) {
        return User.builder()
                .username(signupUser.getUsername())
                .name(signupUser.getName())
                .nickname(signupUser.getNickname())
                .authorities(Collections.singleton(authority))
                .password(passwordEncoded)
                .build();
    }

    public static User SignUpSocialUser(UserReq.SocialSignUp socialSignUp, Authority authority, String passwordEncoded, String profileImgUrl) {
        return User.builder()
                .profileImgUrl(profileImgUrl)
                .password(passwordEncoded)
                .name(socialSignUp.getNickname())
                .nickname(socialSignUp.getNickname())
                .username(socialSignUp.getSocialId())
                .gender(socialSignUp.getGender())
                .authorities(Collections.singleton(authority))
                .loginDate(LocalDateTime.now())
                .build();
    }

    public static UserRes.SignUp SignUpUserRes(UserRes.GenerateToken token, User user, List<String> categoryList) {
        return UserRes.SignUp.builder().userId(user.getId())
                .accessToken(token.getAccessToken())
                .build();
    }

    public static UserRes.MyPage MyPage(User user) {
        return UserRes.MyPage.builder()
                .userId(user.getId())
                .name(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl()).build();
    }
}
