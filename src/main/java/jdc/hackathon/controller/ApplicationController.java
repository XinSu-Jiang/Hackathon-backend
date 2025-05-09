package jdc.hackathon.controller;

import jakarta.persistence.EntityNotFoundException;
import jdc.hackathon.domain.dto.application.ApplicationResponse;
import jdc.hackathon.domain.dto.application.UpdateApplicationRequest;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.Optional;

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
        try {
            appService.cancel(userId, applicationId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        } catch (SecurityException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
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
        try {
            ApplicationResponse res = appService.respond(userId, applicationId, req);
            return ResponseEntity.ok(res);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (SecurityException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    // 4) 내가 신청한 목록 조회 (페이징)
    @GetMapping("/users/me/applications")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Long userId = Optional.ofNullable(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .getUser().getId();

        Page<ApplicationResponse> page = appService.getMyApplications(userId, pageable);
        return ResponseEntity.ok(page);
    }

    // 5) 내 글의 신청 목록 조회 (페이징)
    @GetMapping("/posts/{postId}/applications")
    public ResponseEntity<Page<ApplicationResponse>> getPostApplications(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Long userId = Optional.ofNullable(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .getUser().getId();

        try {
            Page<ApplicationResponse> page = appService.getPostApplications(userId, postId, pageable);
            return ResponseEntity.ok(page);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        } catch (SecurityException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
    }
}
