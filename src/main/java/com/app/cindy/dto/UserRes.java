package com.app.cindy.dto;

import lombok.*;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Token {
        private Long userId; //user 인덱스
        private String accessToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class GenerateToken{
        private String accessToken;
    }

}
