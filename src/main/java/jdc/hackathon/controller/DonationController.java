package jdc.hackathon.controller;

import jdc.hackathon.domain.dto.donation.*;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    // 1) 후원 약정 (pledge)
    @PostMapping("/posts/{postId}/donations")
    public ResponseEntity<DonationResponse> pledge(
            @PathVariable Long postId,
            @Valid @RequestBody CreateDonationRequest req,
            @AuthenticationPrincipal CustomUserDetails user) {

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        DonationResponse resp = donationService.pledge(user.getUser().getId(), postId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // 3) 내 후원 내역 조회
    @GetMapping("/users/me/donations")
    public ResponseEntity<List<DonationResponse>> myDonations(
            @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(
                donationService.getMyDonations(user.getUser().getId()));
    }

    // 4) 내 글에 대한 후원 내역 조회
    @GetMapping("/posts/{postId}/donations")
    public ResponseEntity<List<DonationResponse>> postDonations(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(
                donationService.getPostDonations(user.getUser().getId(), postId));
    }
}

