package com.app.cindy.controller;

import com.app.cindy.exception.BadRequestException;
import com.app.cindy.service.UserService;
import com.app.cindy.common.CommonException;
import com.app.cindy.common.CommonResponse;
import com.app.cindy.dto.UserReq;
import com.app.cindy.dto.UserRes;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;

import static com.app.cindy.constants.CommonResponseStatus.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/signup")
    @ApiOperation(value = "01-01 임시 회원가입 🔑", notes = "")
    public CommonResponse<UserRes.Token> signup(@RequestBody UserReq.SignupUser signupUser) throws IOException {


        if(signupUser.getPassword()==null) throw new BadRequestException(USERS_EMPTY_USER_PASSWORD);
        //if(userService.checkNickName(signupUser.getNickname())) throw new ForbiddenException(USERS_EXISTS_NICKNAME);


        UserRes.Token signUp = userService.signUp(signupUser);

        return CommonResponse.onSuccess(signUp);

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

    /*
    @ApiOperation(value = "닉네임 중복체크", notes = "닉네임 중복체크")
    @GetMapping("/check/nickname")
    public CommonResponse<String> checkNickName(@RequestParam("nickname") String nickName) {
        String result="";
        if(userService.checkNickName(nickName)){
            return new CommonResponse<>(USERS_EXISTS_NICKNAME);
        }
        else{
            result="사용 가능합니다.";
        }
        return new CommonResponse<>(result);

    }

    @ApiOperation(value = "유저 아이디 중복체크", notes = "유저 아이디 중복체크")
    @GetMapping("/check/username")
    public CommonResponse<String> checkUserId(@RequestParam("username") String username){
        String result="";
        if(userService.checkUserId(username)){
            return new CommonResponse<>(USERS_EXISTS_ID);
        }
        else{
            result="사용 가능합니다.";
        }
        return new CommonResponse<>(result);

    }


     */
}
