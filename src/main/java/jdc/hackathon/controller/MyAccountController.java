package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.user.UserRequestDTO;
import jdc.hackathon.domain.dto.user.UserResponseDTO;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MyAccountController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = Long.valueOf(userDetails.getUsername());
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserRequestDTO dto) {
        Long userId = Long.valueOf(userDetails.getUsername());
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMyAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = Long.valueOf(userDetails.getUsername());
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
