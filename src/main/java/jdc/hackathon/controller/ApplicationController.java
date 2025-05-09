package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.application.ApplicationResponse;
import jdc.hackathon.domain.dto.application.UpdateApplicationRequest;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService appService;

    /**
     * 1) 신청하기
     * POST /api/posts/{postId}/applications
     */
    @PostMapping("/posts/{postId}/applications")
    public ResponseEntity<ApplicationResponse> apply(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Long userId = userDetails.getUser().getId();
        ApplicationResponse res = appService.apply(userId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /**
     * 2) 신청 철회
     * DELETE /api/applications/{applicationId}
     */
    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Long userId = userDetails.getUser().getId();
        appService.cancel(userId, applicationId);
        return ResponseEntity.ok().build();
    }

    /**
     * 3) 신청 상태 처리 (수락/거절)
     * PUT /api/applications/{applicationId}
     */
    @PutMapping("/applications/{applicationId}")
    public ResponseEntity<ApplicationResponse> respond(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Long userId = userDetails.getUser().getId();
        ApplicationResponse res = appService.respond(userId, applicationId, req);
        return ResponseEntity.ok(res);
    }

    /**
     * 4) 내가 신청한 목록 조회
     * GET /api/users/me/applications
     */
    @GetMapping("/users/me/applications")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Long userId = userDetails.getUser().getId();
        List<ApplicationResponse> list = appService.getMyApplications(userId);
        return ResponseEntity.ok(list);
    }

    /**
     * 5) 내 글의 신청 목록 조회
     * GET /api/posts/{postId}/applications
     */
    @GetMapping("/posts/{postId}/applications")
    public ResponseEntity<List<ApplicationResponse>> getPostApplications(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Long userId = userDetails.getUser().getId();
        List<ApplicationResponse> list = appService.getPostApplications(userId, postId);
        return ResponseEntity.ok(list);
    }
}
