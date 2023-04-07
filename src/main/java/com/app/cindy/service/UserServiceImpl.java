package com.app.cindy.service;

import com.app.cindy.convertor.UserConvertor;
import com.app.cindy.domain.Authority;
import com.app.cindy.domain.user.UserCategory;
import com.app.cindy.repository.UserCategoryRepository;
import com.app.cindy.repository.UserRepository;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.dto.user.UserRes;
import com.app.cindy.exception.BadRequestException;
import com.app.cindy.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.cindy.constants.CommonResponseStatus.NOT_CORRECT_PASSWORD;
import static com.app.cindy.constants.CommonResponseStatus.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserCategoryRepository userCategoryRepository;



    @Override
    public UserRes.Token logIn(UserReq.LoginUserInfo loginUserInfo){

        if(!checkUserId(loginUserInfo.getUsername())){
            throw new BadRequestException(NOT_EXIST_USER);
        }

        Optional<User> user=userRepository.findByUsername(loginUserInfo.getUsername());
        Long userId = user.get().getId();


        if(!passwordEncoder.matches(loginUserInfo.getPassword(),user.get().getPassword())){
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

    @Override
    public UserRes.Token signUp(UserReq.SignupUser signupUser) {
        Authority authority = UserConvertor.PostAuthroity();
        String passwordEncoded=passwordEncoder.encode(signupUser.getPassword());

        User user = UserConvertor.SignUpUser(signupUser,authority,passwordEncoded);

        Long userId=userRepository.save(user).getId();

        UserRes.GenerateToken token = createToken(userId);

        return new UserRes.Token(userId,token.getAccessToken());
    }

    @Override
    public List<Long> getCategoryList(Long userId) {
        List<UserCategory> category=userCategoryRepository.findByUserId(userId);
        return category.stream().map(e-> e.getId().getCategoryId()).collect(Collectors.toList());
    }
}
