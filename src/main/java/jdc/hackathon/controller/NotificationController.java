package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.notification.NotificationResponseDTO;
import jdc.hackathon.domain.dto.notification.SuccessResponseDTO;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = principal.getId();
        List<NotificationResponseDTO> list = notificationService.getMyNotifications(userId, pageable);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{notifId}/read")
    public ResponseEntity<SuccessResponseDTO> markRead(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long notifId) {
        Long userId = principal.getId();
        notificationService.markAsRead(userId, notifId);
        return ResponseEntity.ok(new SuccessResponseDTO(true));
    }

    @DeleteMapping("/{notifId}")
    public ResponseEntity<SuccessResponseDTO> deleteNotification(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long notifId) {
        Long userId = principal.getId();
        notificationService.deleteNotification(userId, notifId);
        return ResponseEntity.ok(new SuccessResponseDTO(true));
    }
}