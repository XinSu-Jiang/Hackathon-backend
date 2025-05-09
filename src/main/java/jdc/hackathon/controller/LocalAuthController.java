package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.TokenResponseDTO;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.repository.UserRepository;
import jdc.hackathon.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile("local")
@RequiredArgsConstructor
public class LocalAuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /**
     * 로컬 테스트용: userId로 액세스 토큰 발급
     * GET /local-token?userId=4
     */
    @GetMapping("/local-token")
    public TokenResponseDTO devToken(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        String accessToken = jwtTokenProvider.createAccessToken(user);
        return new TokenResponseDTO(accessToken, null);
    }
}
