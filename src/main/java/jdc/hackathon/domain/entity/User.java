package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.entity.common.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_oauth", columnNames = {"provider", "oauth_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(name = "oauth_id", nullable = false, length = 100)
    private String oauthId;

    @Column(length = 50)
    private String nickname;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(length = 255)
    private String introduction;

    @Column(name = "seed_money_balance")
    private Integer seedMoneyBalance = 10000;

    @Column(name = "deok_points", nullable = false)
    @Builder.Default
    private Integer deokPoints = 0;

    public void updateProfile(String nickname, String profileImage, String introduction) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.introduction = introduction;
    }
}