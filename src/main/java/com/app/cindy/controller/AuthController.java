package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.dto.user.UserRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.exception.BaseException;
import com.app.cindy.exception.ForbiddenException;
import com.app.cindy.service.AuthService;
import com.app.cindy.service.S3Service;
import com.app.cindy.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.app.cindy.constants.CommonResponseStatus.*;

@Api(tags = "01-로그인 🔑")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final LoginService logInService;
    private final S3Service s3Service;


    @ApiOperation(value = "01-01 카카오 로그인 테스트용 코드발급 🔑", notes = "유저 카카오 로그인")
    @GetMapping("/kakao")
    public CommonResponse<String> getAccessTokenKakao(@RequestParam String code){
        String accessToken=authService.getKakaoAccessToken(code);
        System.out.println(accessToken);
        return CommonResponse.onSuccess(accessToken);
    }

    @ApiOperation(value = "01-01 카카오 로그인 🔑", notes = "유저 카카오 로그인")
    @ResponseBody
    @PostMapping("/kakao")
    public CommonResponse<UserRes.Token> kakaoLogin(@RequestBody UserReq.SocialLogin SocialLogin) throws BaseException {
        log.info("kakao-login");
        log.info("api = kakao-login 01-08, kakaoAccesstoken={}",SocialLogin.getAccessToken());

        UserRes.Token tokenRes = authService.logInKakaoUser(SocialLogin);
        return CommonResponse.onSuccess(tokenRes);
    }

    @ApiOperation(value = "01-02 소셜 회원가입 🔑", notes = "유저 소셜회원가입")
    @ResponseBody
    @PostMapping(value = "/signup/kakao")
    public CommonResponse<UserRes.SignUp> socialSignUp(@RequestBody UserReq.SocialSignUp socialSignUp) throws BaseException, IOException {
        log.info("post-social-signup");
        log.info("api = social-signup 01-10");

        if(socialSignUp.getCategoryList()==null) throw new BadRequestException(CATEGORY_EMPTY_USERS);
        if(socialSignUp.getSocialId()==null) throw new BadRequestException(USERS_EMPTY_USER_ID);
        if(logInService.checkuserId(socialSignUp.getSocialId()))  throw new ForbiddenException(USERS_EXISTS_SOCIAL_ID);
        UserRes.SignUp signUp = logInService.signUpSocial(socialSignUp);


        return CommonResponse.onSuccess(signUp);

    }
}
