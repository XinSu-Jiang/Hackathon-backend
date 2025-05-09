package jdc.hackathon.service;

import jdc.hackathon.domain.dto.review.ReceivedReviewDTO;
import jdc.hackathon.domain.dto.review.ReviewRequestDTO;
import jdc.hackathon.domain.dto.review.ReviewResponseDTO;
import jdc.hackathon.domain.dto.review.SentReviewDTO;
import jdc.hackathon.domain.entity.DonationPost;
import jdc.hackathon.domain.entity.Review;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.repository.DonationPostRepository;
import jdc.hackathon.domain.repository.ReviewRepository;
import jdc.hackathon.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DonationPostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponseDTO writeReview(Long postId, Long reviewerId, ReviewRequestDTO dto) {
        DonationPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer not found"));
        User reviewee = post.getUser();

        Review review = Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .post(post)
                .comment(dto.getComment())
                .build();
        reviewRepository.save(review);

        return new ReviewResponseDTO(
                review.getId(),
                new ReviewResponseDTO.UserInfoDTO(reviewer.getId(), reviewer.getNickname()),
                new ReviewResponseDTO.UserInfoDTO(reviewee.getId(), reviewee.getNickname()),
                post.getId(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ReceivedReviewDTO> getReceivedReviews(Long userId) {
        return reviewRepository.findByRevieweeId(userId).stream()
                .map(r -> new ReceivedReviewDTO(
                        r.getId(),
                        new ReviewResponseDTO.UserInfoDTO(r.getReviewer().getId(), r.getReviewer().getNickname()),
                        new ReceivedReviewDTO.PostInfoDTO(r.getPost().getId(), r.getPost().getTitle()),
                        r.getComment(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SentReviewDTO> getSentReviews(Long userId) {
        return reviewRepository.findByReviewerId(userId).stream()
                .map(r -> new SentReviewDTO(
                        r.getId(),
                        new ReviewResponseDTO.UserInfoDTO(r.getReviewee().getId(), r.getReviewee().getNickname()),
                        new ReceivedReviewDTO.PostInfoDTO(r.getPost().getId(), r.getPost().getTitle()),
                        r.getComment(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
