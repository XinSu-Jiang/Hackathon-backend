package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.ScheduledNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationRepository extends JpaRepository<ScheduledNotification, Long> {
    List<ScheduledNotification> findByIsSentFalseAndScheduledTimeBefore(LocalDateTime now);
}