package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.enumType.ApplicationStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(name = "uq_ap", columnNames = {"post_id","user_id"}),
        indexes = {
                @Index(name = "idx_ap_post", columnList = "post_id"),
                @Index(name = "idx_ap_user", columnList = "user_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DonationPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "applied_at", updatable = false)
    private LocalDateTime appliedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
}