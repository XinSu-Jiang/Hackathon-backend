package jdc.hackathon.domain.dto.review;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SentReviewDTO {
    private Long id;
    private ReviewResponseDTO.UserInfoDTO reviewee;
    private ReceivedReviewDTO.PostInfoDTO post;
    private String comment;
    private LocalDateTime createdAt;
}