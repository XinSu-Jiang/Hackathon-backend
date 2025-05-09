package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.entity.common.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name = "reviews",
        indexes = @Index(name = "idx_r_reviewee", columnList = "reviewee_id"))
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DonationPost post;

    @Lob
    private String comment;
}