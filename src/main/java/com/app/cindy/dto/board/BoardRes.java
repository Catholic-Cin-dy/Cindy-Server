package com.app.cindy.dto.board;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

public class BoardRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-01 ootd 게시판 조회 👗")
    public static class BoardList{
        @ApiModelProperty(notes = "게시글 ID",required = true,example = "1")
        private Long boardId;

        @ApiModelProperty(notes = "게시글 제목",required = true,example = "Y2K")
        private String title;

        @ApiModelProperty(notes = "작성자",required = true,example = "awesominki")
        private String writer;

        @ApiModelProperty(notes = "게시글 사진 리스트",required = true,example = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fkr.dhgate.com%2Fproduct%2Fy2k-style-european-and-american-loose-street%2F721693421.html&psig=AOvVaw1u68ZP8mfVCnV1Vyu23vhx&ust=1680854969967000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCJCql_HmlP4CFQAAAAAdAAAAABAD")
        private List<String> boardImg;

        @ApiModelProperty(notes = "프로필 이미지",required = true,example = "****")
        private String profileImg;

        @ApiModelProperty(notes = "좋아요 개수",required = true,example = "123")
        private Long likeCount;

        @ApiModelProperty(notes = "댓글 수",required = true,example = "123")
        private Long commentCount;


        @ApiModelProperty(notes = "좋아요 누른 여부",required = true,example = "ture")
        private boolean likeCheck;

        @ApiModelProperty(notes = "작성 시간",required = true,example = "1 시간전")
        private String boardTime;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-02 OOTD  게시판 상세 조회  API Response #FRAME OOTD 04-02")
    public static class BoardDetail{
        @ApiModelProperty(notes = "게시글 ID",required = true,example = "1")
        private Long boardId;

        @ApiModelProperty(notes = "게시글 제목",required = true,example = "Y2K")
        private String title;

        @ApiModelProperty(notes = "게시글 내용",required = true,example = "옷이쁘죠")
        private String content;

        @ApiModelProperty(notes = "작성자 프로필 이미지 url",required = true,example = "이미지 url")
        private String profileImgUrl;

        @ApiModelProperty(notes = "작성자",required = true,example = "austin")
        private String writer;

        @ApiModelProperty(notes = "게시글 사진 리스트",required = true,example = "")
        private List<ImgList> imgList;

        @ApiModelProperty(notes = "좋아요 개수",required = true,example = "123")
        private Long likeCount;

        @ApiModelProperty(notes = "좋아요 누른 여부",required = true,example = "ture")
        private boolean likeCheck;


        @ApiModelProperty(notes = "댓글 수",required = true,example = "123")
        private Long commentCount;

        @ApiModelProperty(notes = "작성 시간",required = true,example = "1 시간전")
        private String boardTime;

        @ApiModelProperty(notes = "자기 게시글 유무",required = true,example = "true")
        private boolean isMy;



    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-02 OOTD 게시판 이미지 리스트 👗 API Response #FRAME OOTD 04-02")
    public static class ImgList{
        @ApiModelProperty(notes = "imgId",required = true,example = "1")
        private Long imgId;
        @ApiModelProperty(notes = "이미지 순서",required = true,example = "1")
        private int sequence;
        @ApiModelProperty(notes = "이미지 url 정보가 들어가요~",required = true,example = "이미지 url")
        private String imgUrl;
        @ApiModelProperty(notes = "이미지 태그 리스트가 들어가요" ,required = true,example = "")
        private List<ImgTagList> imgTags;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-02 OOTD 게시판 이미지 좌표 리스트 👗 API Response #FRAME OOTD 04-02")
    public static class ImgTagList{
        @ApiModelProperty(notes = "imgId",required = true,example = "1")
        private Long imgId;
        @ApiModelProperty(notes = "브랜드 id 값",required = true,example = "1")
        private Long brandId;
        @ApiModelProperty(notes = "브랜드 이름",required = true,example = "프라다")
        private String brandName;
        @ApiModelProperty(notes = "태그 x 좌표",required = true,example = "124")
        private double x;
        @ApiModelProperty(notes = "태그 y 좌표",required = true,example = "1.24")
        private double y;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "04-03 OOTD  게시판 댓글 조회 👗 API Response #FRAME OOTD 04-02")
    public static class BoardComment {
        @ApiModelProperty(notes = "댓글 id",required = true,example = "1")
        private  Long commentId;
        @ApiModelProperty(notes = "작성자 id 값",required = true,example = "1")
        private Long userId;
        @ApiModelProperty(notes = "프로필 이미지",required = true,example = "프로필 이미지 url")
        private String profileImgUrl;
        @ApiModelProperty(notes = "게시글 작성자 이름",required = true,example = "이메누")
        private String nickName;
        @ApiModelProperty(notes = "게시글 내용",required = true,example = "이뻐요")
        private String comment;
        @ApiModelProperty(notes = "게시글 작성 시간",required = true,example = "1시간 전")
        private String commentTime;
        @ApiModelProperty(notes = "자기 댓글 유무",required = true,example = "true")
        private boolean isMy;

    }
}
