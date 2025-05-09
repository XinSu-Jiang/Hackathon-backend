package jdc.hackathon.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenRefreshRequestDTO {
    private String refreshToken;
}
