package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByPostIdAndUserId(Long postId, Long userId);
    Page<Application> findAllByUserId(Long userId, Pageable pageable);
    Page<Application> findAllByPostId(Long postId, Pageable pageable);
    List<Application> findAllByPostId(Long postId);
}
