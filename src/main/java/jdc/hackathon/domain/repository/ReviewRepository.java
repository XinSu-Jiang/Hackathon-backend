package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRevieweeId(Long revieweeId);
    List<Review> findByReviewerId(Long reviewerId);
}