package jdc.hackathon.domain.dto.application;

import jdc.hackathon.domain.enumType.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long postId;
    private UserSummary user;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime respondedAt;

    @Data
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String nickname;
        private String profileImage;
    }
}