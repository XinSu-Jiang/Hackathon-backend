// 4) DTOs
package jdc.hackathon.domain.dto.donation;

import jdc.hackathon.domain.enumType.DonationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationResponse {
    private Long id;
    private SimpleUser donor;
    private Long postId;
    private Integer amount;
    private DonationStatus status;
    private LocalDateTime pledgedAt;
    private LocalDateTime completedAt;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleUser {
        private Long id;
        private String nickname;
        private String profileImage;
    }
}
