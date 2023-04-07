package com.app.cindy.service;

import com.app.cindy.convertor.UserConvertor;
import com.app.cindy.domain.Authority;
import com.app.cindy.domain.pk.UserCategoryPk;
import com.app.cindy.domain.user.User;
import com.app.cindy.domain.user.UserCategory;
import com.app.cindy.dto.user.UserReq;
import com.app.cindy.dto.user.UserRes;
import com.app.cindy.jwt.TokenProvider;
import com.app.cindy.repository.UserCategoryRepository;
import com.app.cindy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AwsS3Service awsS3Service;
    private final UserCategoryRepository userCategoryRepository;


    public UserRes.GenerateToken createToken(Long userId) {
        String accessToken=tokenProvider.createToken(userId);

        return new UserRes.GenerateToken(accessToken);
    }

    public boolean checkuserId(String socialId) {
        return userRepository.existsByUsername(socialId);
    }

    public boolean checkNickName(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public UserRes.SignUp signUpSocial(UserReq.SocialSignUp socialSignUp) throws IOException {
        Authority authority = UserConvertor.PostAuthroity();
        String passwordEncoded=passwordEncoder.encode(socialSignUp.getSocialId());
        String profileImgUrl;

        profileImgUrl = socialSignUp.getProfileImgUrl();

        if(socialSignUp.getGender().equals("male"))
            socialSignUp.setGender("남자");
        else
            socialSignUp.setGender("여자");

        User user = UserConvertor.SignUpSocialUser(socialSignUp,authority,passwordEncoded,profileImgUrl);


        Long userId=userRepository.save(user).getId();

        saveUserCategoryList(userId,socialSignUp.getCategoryList());

        List<String> categoryList = getCategoryNameList(socialSignUp.getCategoryList());


        UserRes.GenerateToken token = createToken(userId);

        return UserConvertor.SignUpUserRes(token,user,categoryList);
    }
    @Transactional(rollbackFor = SQLException.class)
    public void saveUserCategoryList(Long userId,List<Long> userCategoryList){

        List<UserCategory> userCategoryArrayList=new ArrayList<>();

        for(Long categoryId : userCategoryList){
            UserCategory userCategory=UserCategory.builder().id(new UserCategoryPk(userId,categoryId)).build();
            userCategoryArrayList.add(userCategory);
        }

        userCategoryRepository.saveAll(userCategoryArrayList);

    }

    public List<String> getCategoryNameList(List<Long> categoryList){
        List<String> categoryNameList=new ArrayList<>();

        for(int i=0;i<categoryList.size();i++){
            if(categoryList.get(i)==1){
                categoryNameList.add("미니멀");
            }
            else if(categoryList.get(i)==2){
                categoryNameList.add("캐주얼");
            }
            else if(categoryList.get(i)==3){
                categoryNameList.add("시티보이");
            }
            else if(categoryList.get(i)==4){
                categoryNameList.add("스트릿");
            }
            else if(categoryList.get(i)==5){
                categoryNameList.add("빈티지");
            }
            else if(categoryList.get(i)==6){
                categoryNameList.add("페미닌");
            }
        }
        return categoryNameList;

    }
}
