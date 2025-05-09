package jdc.hackathon.service;

import jdc.hackathon.domain.dto.notification.NotificationResponseDTO;
import jdc.hackathon.domain.entity.Notification;
import jdc.hackathon.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<NotificationResponseDTO> getMyNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long userId, Long notifId) {
        Notification nt = notificationRepository.findByIdAndUserId(notifId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        if (!Boolean.TRUE.equals(nt.getIsRead())) {
            nt.setIsRead(true);
            notificationRepository.save(nt);
        }
    }

    public void deleteNotification(Long userId, Long notifId) {
        Notification nt = notificationRepository.findByIdAndUserId(notifId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        notificationRepository.delete(nt);
    }

    private NotificationResponseDTO toDto(Notification nt) {
        return NotificationResponseDTO.builder()
                .id(nt.getId())
                .title(nt.getTitle())
                .message(nt.getMessage())
                .linkUrl(nt.getLinkUrl())
                .isRead(nt.getIsRead())
                .createdAt(nt.getCreatedAt().format(ISO_FMT))
                .build();
    }
}