package com.app.cindy.domain.board;

import com.app.cindy.domain.BaseEntity;
import com.app.cindy.domain.user.User;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Board")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@ToString
public class Board extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

    @Column(name="user_id")
    private Long userId;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;

    @Column(name="latitude")
    private double latitude;

    @Column(name="longitude")
    private double longitude;

    @Column(name="view")
    @ColumnDefault(value= "0")
    private Long view;

//    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
//    private final List<BoardImg> images = new ArrayList<>();

    public void updateBoard(String title,String content,double latitude, double longitude){
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
