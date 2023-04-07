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

        @ApiModelProperty(notes = "좋아요 개수",required = true,example = "123")
        private Long likeCount;

        @ApiModelProperty(notes = "댓글 수",required = true,example = "123")
        private Long commentCount;

        @ApiModelProperty(notes = "작성 시간",required = true,example = "1 시간전")
        private String boardTime;


    }
}
