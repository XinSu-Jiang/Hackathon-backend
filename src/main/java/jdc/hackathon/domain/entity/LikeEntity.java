package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.entity.common.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name = "likes",
        uniqueConstraints = @UniqueConstraint(name = "uq_likes", columnNames = {"user_id","post_id"}),
        indexes = {
                @Index(name = "idx_likes_user", columnList = "user_id"),
                @Index(name = "idx_likes_post", columnList = "post_id")
        })
@IdClass(LikeId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LikeEntity extends BaseTimeEntity {
    @Id
    private Long userId;

    @Id
    private Long postId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DonationPost post;


}
