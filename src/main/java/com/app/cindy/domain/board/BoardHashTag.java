package com.app.cindy.domain.board;

import com.app.cindy.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "BoardHashTag")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class BoardHashTag extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false,insertable=false, updatable=false)
    private Board board;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "tag")
    private String tag;
}
