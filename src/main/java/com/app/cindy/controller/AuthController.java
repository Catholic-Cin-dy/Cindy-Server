package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.dto.UserReq;
import com.app.cindy.dto.UserRes;
import com.app.cindy.exception.BaseException;
import com.app.cindy.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "01-로그인 🔑")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;


    @ApiOperation(value = "01-08 카카오 로그인 테스트용 코드발급 🔑", notes = "유저 카카오 로그인")
    @GetMapping("/kakao")
    public CommonResponse<String> getAccessTokenKakao(@RequestParam String code){
        String accessToken=authService.getKakaoAccessToken(code);
        System.out.println(accessToken);
        return CommonResponse.onSuccess(accessToken);
    }

    @ApiOperation(value = "01-08 카카오 로그인 🔑", notes = "유저 카카오 로그인")
    @ResponseBody
    @PostMapping("/kakao")
    public CommonResponse<UserRes.Token> kakaoLogin(@RequestBody UserReq.SocialLogin SocialLogin) throws BaseException {
        log.info("kakao-login");
        log.info("api = kakao-login 01-08, kakaoAccesstoken={}",SocialLogin.getAccessToken());

        UserRes.Token tokenRes = authService.logInKakaoUser(SocialLogin);
        return CommonResponse.onSuccess(tokenRes);
    }
}
