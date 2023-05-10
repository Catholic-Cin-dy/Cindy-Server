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
    @ApiModel(value = "04-04 게시판 작성 Request👗")
    public static class PostBoard{
        @ApiModelProperty(notes = "게시판 제목", required = true, example = "오늘의 ootd!")
        private String title;

        @ApiModelProperty(notes = "해당 API는 포스트맨으로 해주세요!", required = true, example = "포스트맨으로 해야함!")
        List<img> imgList;
        @ApiModelProperty(notes = "게시판 글 내용", required = true, example = "고프코어 느낌을 내봤습니다~")
        private String content;

        @ApiModelProperty(notes = "위도", required = true, example = "12.34")
        private double latitude;

        @ApiModelProperty(notes = "경도", required = true, example = "56.78")
        private double longitude;

        @ApiModelProperty(notes="태그 리스트",required = true,example = "[\"샤넬\",\"샤넬 이뻐요\"]")
        private List<String> tags;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ApiModel(value = "04-03 게시판 수정 Request🔑")
    public static class UpdateBoard{
        @ApiModelProperty(notes = "게시판 제목", required = true, example = "오늘의 ootd!")
        private String title;

        @ApiModelProperty(notes = "해당 API는 포스트맨으로 해주세요!", required = true, example = "포스트맨으로 해야함!")
        List<img> imgList;
        @ApiModelProperty(notes = "게시판 글 내용", required = true, example = "고프코어 느낌을 내봤습니다~")
        private String content;

        @ApiModelProperty(notes = "위도", required = true, example = "12.34")
        private double latitude;

        @ApiModelProperty(notes = "경도", required = true, example = "56.78")
        private double longitude;

        @ApiModelProperty(notes = "태그 리스트", required = true, example = "[\"안녕\",\"OOTD\"]")
        private List<String> tags;

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

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ApiModel(value = "04-07 게시판 댓글 달기 Request👗")
    public static class Comment {
        @ApiModelProperty(notes = "1", required = true, example = "게시판 아이디")
        private Long boardId;
        @ApiModelProperty(notes = "댓글 내용", required = true, example = "오늘의 ootd 이뻐요")
        private String comment;
    }
}
