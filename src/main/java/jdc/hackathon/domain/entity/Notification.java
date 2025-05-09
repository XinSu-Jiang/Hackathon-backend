package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.common.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name = "notifications",
        indexes = @Index(name = "idx_ntf_user", columnList = "user_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(length = 100)
    private String title;

    @Lob
    private String message;

    @Column(name = "link_url", length = 255)
    private String linkUrl;

    @Column(name = "is_read")
    private Boolean isRead = false;
}
