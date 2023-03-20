package com.app.cindy.service;

import com.app.cindy.repository.UserRepository;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.UserReq;
import com.app.cindy.dto.UserRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.app.cindy.constants.CommonResponseStatus.NOT_CORRECT_PASSWORD;
import static com.app.cindy.constants.CommonResponseStatus.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;



    @Override
    public UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo){

        if(!checkUserId(loginUserInfo.getUsername())){
            throw new BadRequestException(NOT_EXIST_USER);
        }

        User user=userRepository.findByUsername(loginUserInfo.getUsername());
        Long userId = user.getId();

        if(!passwordEncoder.matches(loginUserInfo.getPassword(),user.getPassword())){
            throw new BadRequestException(NOT_CORRECT_PASSWORD);
        }




        UserRes.GenerateToken generateToken=createToken(userId);


        return new UserRes.Token(userId,generateToken.getAccessToken());
    }

    private UserRes.GenerateToken createToken(Long userId) {
        String accessToken=tokenProvider.createToken(userId);


        return new UserRes.GenerateToken(accessToken);
    }


    @Override
    public boolean checkUserId(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    public boolean checkNickName(String nickname) {
        return userRepository.existsByNickname(nickname);
    }



    @Override
    public void updateFcmToken(Long userId, String token) {
        Optional<User> user = userRepository.findById(userId);

        user.get().updateToken(token);

        userRepository.save(user.get());
    }
}
