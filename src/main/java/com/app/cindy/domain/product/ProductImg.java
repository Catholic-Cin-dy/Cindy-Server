package com.app.cindy.domain.product;

import com.app.cindy.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ProductImg")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class ProductImg  extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false,insertable=false, updatable=false)
    private Product product;

    @Column(name="product_id")
    private Long productId;


}
