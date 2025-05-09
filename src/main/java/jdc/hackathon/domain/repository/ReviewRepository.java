package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByRevieweeId(Long userId, Pageable pageable);
    Page<Review> findByReviewerId(Long userId, Pageable pageable);
}