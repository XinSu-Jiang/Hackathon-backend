package jdc.hackathon.domain.dto.review;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReceivedReviewDTO {
    private Long id;
    private ReviewResponseDTO.UserInfoDTO reviewer;
    private PostInfoDTO post;
    private String comment;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class PostInfoDTO {
        private Long id;
        private String title;
    }
}
