package com.app.cindy.domain.community;

import com.app.cindy.domain.BaseEntity;
import com.app.cindy.domain.product.Brand;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "BoardImgTag")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class BoardImgTag extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="x")
    private double x;

    @Column(name="y")
    private double y;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false,insertable=false, updatable=false)
    private Brand brand;

    @Column(name="brand_id")
    private Long brandId;

    @ManyToOne
    @JoinColumn(name = "img_id", nullable = false,insertable=false, updatable=false)
    private BoardImg boardImg;

    @Column(name="img_id")
    private Long imgId;

}
