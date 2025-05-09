package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByPostIdAndUserId(Long postId, Long userId);
    List<Application> findAllByUserId(Long userId);
    List<Application> findAllByPostId(Long postId);
}
