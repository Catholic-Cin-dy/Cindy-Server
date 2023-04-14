package com.app.cindy.dto.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class BoardReq {

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ApiModel(value = "04-04 게시판 작성 Request🔑")
    public static class PostBoard{
        @ApiModelProperty(notes = "게시판 제목", required = true, example = "오늘의 ootd!")
        private String title;

        @ApiModelProperty(notes = "이미지 객체..", required = true, example = "형식을 모르겠습니다.")
        List<img> imgList;
        @ApiModelProperty(notes = "게시판 글 내용", required = true, example = "고프코어 느낌을 내봤습니다~")
        private String content;

        @ApiModelProperty(notes = "위도", required = true, example = "12.34")
        private double latitude;

        @ApiModelProperty(notes = "경도", required = true, example = "56.78")
        private double longitude;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ApiModel(value = "imgTagList")
    public static class imgTag{
        private Long brandId;
        private Long imgId;
        private double x;
        private double y;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class img{
        private Long brandId;
        private Long imgId;
        private double x;
        private double y;
    }
}
