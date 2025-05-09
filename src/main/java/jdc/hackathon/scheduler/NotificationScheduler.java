package jdc.hackathon.scheduler;

import jdc.hackathon.domain.entity.Notification;
import jdc.hackathon.domain.entity.ScheduledNotification;
import jdc.hackathon.domain.repository.NotificationRepository;
import jdc.hackathon.domain.repository.ScheduledNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final ScheduledNotificationRepository schedRepo;
    private final NotificationRepository notifRepo;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Scheduled(cron = "0 0 * * * *") // 매시간 정각 실행
    public void sendPendingNotifications() {
        List<ScheduledNotification> list = schedRepo.findByIsSentFalseAndScheduledTimeBefore(LocalDateTime.now());
        for (ScheduledNotification sn : list) {
            var post = sn.getPost();
            var user = post.getUser();
            // 칭찬 요청 알림 생성
            String dateStr = post.getDonationDate().format(DATE_FMT);
            String title = "칭찬 요청";
            String message = String.format("%s에 진행된 \"%s\"에 대한 칭찬을 남겨주세요!", dateStr, post.getTitle());
            String linkUrl = "/posts/" + post.getId() + "/reviews";

            Notification nt = Notification.builder()
                    .user(user)
                    .title(title)
                    .message(message)
                    .linkUrl(linkUrl)
                    .isRead(false)
                    .build();

            notifRepo.save(nt);
            // 발송 처리
            sn.setIsSent(true);
            schedRepo.save(sn);
        }
    }
}
