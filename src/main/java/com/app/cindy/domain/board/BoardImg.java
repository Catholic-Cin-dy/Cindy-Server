package com.app.cindy.domain.board;

import com.app.cindy.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BoardImg")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@ToString
public class BoardImg extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="img_url")
    private String imgUrl;

    @Column(name="sequence")
    private int sequence;

    @ManyToOne
    private Board board;

    @Column(name="board_id", insertable = false, updatable = false)
    private Long boardId;

    public BoardImg(String imgUrl,Long BoardId,int sequence){
        this.imgUrl = imgUrl;
        this.boardId = BoardId;
        this.sequence = sequence;
    }

}
