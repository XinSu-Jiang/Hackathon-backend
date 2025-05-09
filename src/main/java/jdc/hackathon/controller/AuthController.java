package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.TokenRefreshRequestDTO;
import jdc.hackathon.domain.dto.TokenResponseDTO;
import jdc.hackathon.domain.entity.RefreshToken;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.repository.RefreshTokenRepository;
import jdc.hackathon.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효한 리프레시 토큰이 아닙니다.");
        }

        RefreshToken saved = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다.")
                );

        if (saved.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다.");
        }

        User user = saved.getUser();
        String newAccess  = jwtTokenProvider.createAccessToken(user);
        String newRefresh = jwtTokenProvider.createRefreshToken();

        // 토큰/만료일 갱신
        saved.setToken(newRefresh);
        saved.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(saved);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefresh)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new TokenResponseDTO(newAccess, null));
    }

    @PostMapping("/refresh-body")
    public ResponseEntity<TokenResponseDTO> refreshAccessTokenBody(
            @RequestBody TokenRefreshRequestDTO request,
            HttpServletResponse response
    ) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효한 리프레시 토큰이 아닙니다.");        }
        RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다.")
                );
        if (savedToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다.");
        }
        User user = savedToken.getUser();
        String newAccessToken  = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        savedToken.setToken(newRefreshToken);
        savedToken.setExpiredAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(savedToken);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        // 7) 새 액세스 토큰만 반환
        return ResponseEntity.ok(new TokenResponseDTO(newAccessToken, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그아웃 권한이 없습니다.");
        }
        String access = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(access)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다.");
        }

        Optional.ofNullable(refreshToken)
                .flatMap(refreshTokenRepository::findByToken)
                .ifPresent(refreshTokenRepository::delete);

        Cookie del = new Cookie("refreshToken", null);
        del.setHttpOnly(true);
        del.setSecure(true);
        del.setPath("/");
        del.setMaxAge(0);
        response.addCookie(del);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout/all")
    @Transactional
    public ResponseEntity<Void> logoutAll(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그아웃 권한이 없습니다.");
        }
        String access = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(access)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다.");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(access);
        refreshTokenRepository.deleteByUserId(userId);

        Cookie del = new Cookie("refreshToken", null);
        del.setHttpOnly(true);
        del.setSecure(true);
        del.setPath("/");
        del.setMaxAge(0);
        response.addCookie(del);

        return ResponseEntity.ok().build();
    }
}
