package com.app.cindy.domain.user;

import com.app.cindy.domain.pk.BrandPreferPk;
import com.app.cindy.domain.pk.BrandStarPk;
import com.app.cindy.domain.product.Brand;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "BrandPrefer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class BrandPrefer {
    @EmbeddedId
    private BrandPreferPk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false,insertable=false, updatable=false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

    @Column(name="score")
    private int score;
}
