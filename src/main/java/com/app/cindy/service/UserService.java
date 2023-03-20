package com.app.cindy.service;

import com.app.cindy.common.CommonException;
import com.app.cindy.dto.UserReq;
import com.app.cindy.dto.UserRes;

public interface UserService {
    UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo);


    boolean checkUserId(String username);

    boolean checkNickName(String nickname);


    void updateFcmToken(Long userId, String token);
}
