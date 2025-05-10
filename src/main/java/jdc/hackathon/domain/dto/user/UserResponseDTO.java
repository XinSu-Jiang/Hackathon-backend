package jdc.hackathon.domain.dto.user;

import jdc.hackathon.domain.enumType.BadgeType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class UserResponseDTO {
    private Long id;
    private String provider;
    private String oauthId;
    private String nickname;
    private String profileImage;
    private String introduction;
    private Integer deokPoints;
    private Integer seedMoneyBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<BadgeType> badges;
}
