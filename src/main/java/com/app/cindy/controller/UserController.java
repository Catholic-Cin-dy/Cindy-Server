package com.app.cindy.controller;

import com.app.cindy.convertor.UserConvertor;
import com.app.cindy.domain.user.User;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.service.S3Service;
import com.app.cindy.service.UserService;
import com.app.cindy.common.CommonResponse;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.dto.user.UserRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Multipart;

import javax.validation.Valid;

import java.io.IOException;

import static com.app.cindy.constants.CommonResponseStatus.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Api(tags = "05-유저 관련 🚹 API")

public class UserController {

    /*
    @PostMapping(value = "/signup")
    @ApiOperation(value = "01-01 임시 회원가입 🔑", notes = "")
    public CommonResponse<UserRes.Token> signup(@RequestBody UserReq.SignupUser signupUser) throws IOException {


        if(signupUser.getPassword()==null) throw new BadRequestException(USERS_EMPTY_USER_PASSWORD);
        //if(userService.checkNickName(signupUser.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);


        UserRes.Token signUp = userService.signUp(signupUser);

        return CommonResponse.onSuccess(signUp);

    }

    @PostMapping(value = "/img")
    @ApiOperation(value = "01-01 임시 회원가입 🔑", notes = "")
    public CommonResponse<String> uploadTest(@RequestParam MultipartFile multipart) throws IOException {

        s3Service.uploadImg(multipart);

        return CommonResponse.onSuccess("이미지 저장");

    }



    @ApiOperation(value = "로그인", notes = "로그인")
    @PostMapping("/login")
    public CommonResponse<UserRes.Token> login(@Valid @RequestBody UserReq.LoginUserInfo loginUserInfo){
        if(loginUserInfo.getUsername()==null){
            throw new BadRequestException(USERS_EMPTY_USER_ID);
        }
        if(loginUserInfo.getPassword()==null){
            throw new BadRequestException(USERS_EMPTY_USER_PASSWORD);
        }


        UserRes.Token token = userService.logIn(loginUserInfo);
        return CommonResponse.onSuccess(token);

    }

     */

    @ApiOperation(value="마이페이지 정보 조회",notes="마이페이지 조회시 정보조회")
    @GetMapping("/my")
    public CommonResponse<UserRes.MyPage> getMyPage(@AuthenticationPrincipal User user){
        UserRes.MyPage myPage = UserConvertor.MyPage(user);

        return CommonResponse.onSuccess(myPage);
    }

}
