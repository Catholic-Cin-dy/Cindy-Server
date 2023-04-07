package com.app.cindy.service;

import com.app.cindy.dto.user.UserReq;
import com.app.cindy.dto.user.UserRes;

import java.util.List;

public interface UserService {
    UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo);


    boolean checkUserId(String username);

    boolean checkNickName(String nickname);


    void updateFcmToken(Long userId, String token);

    UserRes.Token signUp(UserReq.SignupUser signupUser);

    List<Long> getCategoryList(Long userId);
}
