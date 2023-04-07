package com.app.cindy.domain.board;

import com.app.cindy.domain.pk.BoardLikePk;
import com.app.cindy.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "BoardLike")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class BoardLike {
    @EmbeddedId
    private BoardLikePk id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false,insertable=false, updatable=false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

}
