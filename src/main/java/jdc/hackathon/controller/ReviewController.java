package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.review.ReceivedReviewDTO;
import jdc.hackathon.domain.dto.review.ReviewRequestDTO;
import jdc.hackathon.domain.dto.review.ReviewResponseDTO;
import jdc.hackathon.domain.dto.review.SentReviewDTO;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 1) 칭찬 작성
    @PostMapping("/posts/{postId}/reviews")
    public ReviewResponseDTO writeReview(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReviewRequestDTO dto
    ) {
        Long reviewerId = userDetails.getUser().getId();
        return reviewService.writeReview(postId, reviewerId, dto);
    }

    // 2) 내가 받은 칭찬 조회
    @GetMapping("/users/me/reviews/received")
    public List<ReceivedReviewDTO> getReceived(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        return reviewService.getReceivedReviews(userId);
    }

    // 3) 내가 작성한 칭찬 조회
    @GetMapping("/users/me/reviews/sent")
    public List<SentReviewDTO> getSent(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        return reviewService.getSentReviews(userId);
    }


}