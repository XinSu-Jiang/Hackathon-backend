package jdc.hackathon.domain.dto.user;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
public class UserDto {
    private Long id;
    private String nickname;
    private String profileImage;
    private String introduction;
}
