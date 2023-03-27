package com.app.cindy.convertor;

import com.app.cindy.domain.Authority;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.UserReq;

import java.util.Collections;

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
}
