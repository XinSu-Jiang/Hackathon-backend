package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.entity.common.BaseTimeEntity;
import jdc.hackathon.domain.enumType.ScheduledType;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_notifications",
        indexes = @Index(name = "idx_sch_post", columnList = "post_id"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScheduledNotification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DonationPost post;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ScheduledType type;

    @Column(name = "is_sent")
    private Boolean isSent = false;


}
