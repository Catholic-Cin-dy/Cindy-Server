package com.app.cindy.service;

import com.app.cindy.constants.Constants;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.UserReq;
import com.app.cindy.dto.UserRes;
import com.app.cindy.exception.BaseException;
import com.app.cindy.exception.ForbiddenException;
import com.app.cindy.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;

import java.util.*;

import static com.app.cindy.constants.CommonResponseStatus.KAKAO_SERVER_ERROR;
import static com.app.cindy.constants.CommonResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final LoginService loginService;

    public String getKakaoAccessToken(String code) {
        String access_Token="";
        String refresh_Token;
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=424b46278da8131aa968c7b90935a465" + // TODO REST_API_KEY 입력
                    "&redirect_uri=http://localhost:9000/auth/kakao" + // TODO 인가코드 받은 redirect_uri 입력
                    "&code=" + code;
            bw.write(sb);
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = JsonParser.parseString(result.toString());

            access_Token = element.getAsJsonObject().get("access_token").getAsString();

            System.out.println("access_token : " + access_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public UserRes.Token logInKakaoUser(UserReq.SocialLogin SocialLogin) throws ForbiddenException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + SocialLogin.getAccessToken()); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line ;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            Long id = element.getAsJsonObject().get("id").getAsLong();


            String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();


            String profileImgUrl=element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();
            String gender=element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("gender").getAsString();


            HashMap<String,String> kakao = new HashMap<String,String>();
            kakao.put("kakaoId",id.toString());
            kakao.put("profileImgUrl",profileImgUrl);
            kakao.put("nickname",name);
            kakao.put("gender",gender);




            //회원 존재 X -> 예외처리(카카오 id, 프로필 이미지)
            User user=userRepository.findByUsername(String.valueOf(id)).orElseThrow(() ->
                    new BaseException(USER_NOT_FOUND, kakao));

            br.close();
            Long userId = user.getId();


            UserRes.GenerateToken generateToken = loginService.createToken(userId);


            return new UserRes.Token(userId, generateToken.getAccessToken());


        } catch (IOException e) {
            throw new ForbiddenException(KAKAO_SERVER_ERROR);
        }
    }
}
