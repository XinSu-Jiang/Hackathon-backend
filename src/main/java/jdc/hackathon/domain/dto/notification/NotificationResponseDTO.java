package jdc.hackathon.domain.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String message;
    private String linkUrl;
    private Boolean isRead;
    private String createdAt; // ISO-8601 format
}
