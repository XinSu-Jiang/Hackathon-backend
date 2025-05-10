package jdc.hackathon.controller;

import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/me/analysis")
    public ResponseEntity<AnalysisResponse> analyze(@AuthenticationPrincipal CustomUserDetails user) {
        Long userId = user.getUser().getId();
        String summary = analysisService.analyzeUserActivity(userId);
        return ResponseEntity.ok(new AnalysisResponse(summary));
    }

    @GetMapping("/{userId}/analysis")
    public ResponseEntity<AnalysisResponse> getUserAnalysis(@PathVariable Long userId) {
        String summary = analysisService.analyzeUserActivity(userId);
        return ResponseEntity.ok(new AnalysisResponse(summary));
    }
    public static record AnalysisResponse(String summary) {}
}