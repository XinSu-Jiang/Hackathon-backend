package jdc.hackathon.domain.dto.user;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor  // for Jackson
@AllArgsConstructor @Builder
public class UserRequestDTO {
    @Size(max = 50, message = "닉네임은 최대 50자입니다.")
    private String nickname;

    @Size(max = 255, message = "프로필 이미지 URL은 최대 255자입니다.")
    private String profileImage;

    @Size(max = 255, message = "자기소개는 최대 255자입니다.")
    private String introduction;
}
