package com.app.cindy.domain.user;

import com.app.cindy.domain.Category;
import com.app.cindy.domain.pk.UserCategoryPk;
import com.app.cindy.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "UserCategory")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class UserCategory {

    @EmbeddedId
    private UserCategoryPk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,insertable=false, updatable=false)
    private Category category;
}
