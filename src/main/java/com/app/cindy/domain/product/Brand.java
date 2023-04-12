package com.app.cindy.domain.product;

import com.app.cindy.domain.board.BoardImgTag;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Brand")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Brand {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;


    @OneToMany(fetch = FetchType.LAZY)
    private List<Product> product=new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<BoardImgTag> boardImgTag=new ArrayList<>();

}
