package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.user.UserDto;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 공개 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable Long userId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long viewerId = (userDetails != null)
                ? Long.valueOf(userDetails.getUsername())
                : null;

        return ResponseEntity.ok(userService.getPublicProfile(userId, viewerId));
    }
}
