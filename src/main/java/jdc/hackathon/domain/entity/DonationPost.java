package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import jdc.hackathon.domain.enumType.District;
import jdc.hackathon.domain.enumType.PostCategory;
import jdc.hackathon.domain.enumType.PostStatus;
import lombok.*;
import jdc.hackathon.domain.entity.common.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "donation_posts",
        indexes = {
                @Index(name = "idx_dp_user", columnList = "user_id"),
                @Index(name = "idx_dp_status", columnList = "status"),
                @Index(name = "idx_dp_category", columnList = "status,category")
        })
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DonationPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PostCategory category;

    @Column(length = 100)
    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private District location;

    @Column(name = "recruitment_start")
    private LocalDateTime recruitmentStart;

    @Column(name = "recruitment_end")
    private LocalDateTime recruitmentEnd;

    @Column(name = "donation_date")
    private LocalDateTime donationDate;

    @Column
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PostStatus status = PostStatus.RECRUITING;

    @Column(name = "is_donation_open")
    private Boolean isDonationOpen = false;

    @Column(name = "max_amount")
    private Integer maxAmount;

    @Builder.Default
    @Column(name = "current_person_count", nullable = false)
    private Integer currentPersonCount = 0;

    @Builder.Default
    @Column(name = "current_funding_amount", nullable = false)
    private Integer currentFundingAmount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Donation> donations = new HashSet<>();

    public int getCurrentPersonCount() {
        return applications.size();
    }

    public int getCurrentFundingAmount() {
        return donations.stream()
                .mapToInt(Donation::getAmount)
                .sum();
    }
}
