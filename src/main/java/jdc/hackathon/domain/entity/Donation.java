package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.enumType.DonationStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "donations",
        indexes = {
                @Index(name = "idx_don_donor", columnList = "donor_id"),
                @Index(name = "idx_don_post", columnList = "post_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private User donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DonationPost post;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private DonationStatus status = DonationStatus.PLEDGED;

    @CreationTimestamp
    @Column(name = "pledged_at", updatable = false)
    private LocalDateTime pledgedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}

