package com.app.cindy.service;

import com.app.cindy.domain.user.User;
import com.app.cindy.dto.UserRes;
import com.app.cindy.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final TokenProvider tokenProvider;
    public UserRes.GenerateToken createToken(Long userId) {
        String accessToken=tokenProvider.createToken(userId);

        return new UserRes.GenerateToken(accessToken);
    }

}
