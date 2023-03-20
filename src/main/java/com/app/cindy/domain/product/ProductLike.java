package com.app.cindy.domain.product;

import com.app.cindy.domain.community.Board;
import com.app.cindy.domain.pk.ProductLikePk;
import com.app.cindy.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "ProductLike")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class ProductLike {

    @EmbeddedId
    private ProductLikePk id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false,insertable=false, updatable=false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;
}
