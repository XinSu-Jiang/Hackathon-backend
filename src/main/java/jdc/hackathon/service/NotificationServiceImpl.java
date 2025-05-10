package jdc.hackathon.service;

import jdc.hackathon.domain.dto.notification.NotificationResponseDTO;
import jdc.hackathon.domain.entity.Notification;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.enumType.ApplicationStatus;
import jdc.hackathon.domain.enumType.DonationStatus;
import jdc.hackathon.domain.repository.NotificationRepository;
import jdc.hackathon.domain.repository.UserRepository;
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
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;  // ← 추가
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

    @Override
    public void sendApplicationReceived(Long toUserId,
                                        Long applicationId,
                                        Long postId,
                                        String applicantName) {
        User to = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + toUserId));

        Notification nt = Notification.builder()
                .user(to)
                .title("새 지원 요청이 도착했습니다")
                .message(applicantName + "님이 귀하의 게시글에 지원했습니다.")
                .linkUrl("/posts/" + postId + "/applications")   // 프론트 라우트에 맞춰 조정
                .build();

        notificationRepository.save(nt);
    }

    @Override
    public void sendApplicationResult(Long toUserId,
                                      Long applicationId,
                                      Long postId,
                                      ApplicationStatus result) {
        User to = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + toUserId));

        String title = result == ApplicationStatus.ACCEPTED
                ? "지원이 승인되었습니다"
                : "지원이 거절되었습니다";
        String message = result == ApplicationStatus.ACCEPTED
                ? "축하합니다! 지원이 승인되었습니다."
                : "안타깝게도 지원이 거절되었습니다.";

        Notification nt = Notification.builder()
                .user(to)
                .title(title)
                .message(message)
                .linkUrl("/my/applications")   // 혹은 `/posts/…/applications` 등
                .build();

        notificationRepository.save(nt);
    }

    @Override
    public void sendDonationReceived(Long toUserId,
                                     Long donationId,
                                     Long postId,
                                     int amount,
                                     String donorName) {
        User to = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + toUserId));

        Notification nt = Notification.builder()
                .user(to)
                .title("새 후원 약정이 도착했습니다")
                .message(donorName + "님이 " + amount + "원을 약정했습니다.")
                .linkUrl("/posts/" + postId + "/donations")
                .build();

        notificationRepository.save(nt);
    }

    @Override
    public void sendDonationResult(Long toUserId,
                                   Long donationId,
                                   Long postId,
                                   DonationStatus result) {
        User to = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + toUserId));

        String title = result == DonationStatus.COMPLETED
                ? "후원이 완료되었습니다"
                : "후원이 환불되었습니다";
        String message = result == DonationStatus.COMPLETED
                ? "후원이 정상 처리되어 포인트가 차감되었습니다."
                : "잔액 부족으로 후원이 환불 처리되었습니다.";

        Notification nt = Notification.builder()
                .user(to)
                .title(title)
                .message(message)
                .linkUrl("/my/donations")
                .build();

        notificationRepository.save(nt);
    }

    @Override
    public void sendPostFull(Long toUserId, Long postId) {
        User to = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + toUserId));

        Notification nt = Notification.builder()
                .user(to)
                .title("모집 인원이 가득 찼습니다")
                .message("게시글의 모집 인원이 모두 채워져 상태가 ‘FULL’ 로 변경되었습니다.")
                .linkUrl("/posts/" + postId)
                .build();

        notificationRepository.save(nt);
    }

    @Override
    public void sendPostFunded(Long toUserId, Long postId) {
        User to = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + toUserId));

        Notification nt = Notification.builder()
                .user(to)
                .title("목표 금액이 달성되었습니다")
                .message("게시글의 후원 총액이 목표치를 넘겨 상태가 ‘CLOSED’ 로 변경되었습니다.")
                .linkUrl("/posts/" + postId)
                .build();

        notificationRepository.save(nt);
    }
}

