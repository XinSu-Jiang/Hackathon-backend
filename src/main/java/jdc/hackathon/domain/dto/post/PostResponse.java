package jdc.hackathon.domain.dto.post;

import jdc.hackathon.domain.enumType.District;
import jdc.hackathon.domain.enumType.PostCategory;
import jdc.hackathon.domain.enumType.PostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private UserSummary author;
    private PostCategory category;
    private String title;
    private String description;
    private District location;
    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;
    private LocalDateTime donationDate;
    private Integer capacity;
    private Integer currentPersonCount;
    private Boolean isDonationOpen;
    private Integer maxAmount;
    private PostStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummary {
        private Long id;
        private String nickname;
        private String profileImage;
    }
}