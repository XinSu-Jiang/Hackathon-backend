package jdc.hackathon.domain.dto.review;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private UserInfoDTO reviewer;
    private UserInfoDTO reviewee;
    private Long postId;
    private String comment;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class UserInfoDTO {
        private Long id;
        private String nickname;
    }
}