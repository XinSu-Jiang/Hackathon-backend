package jdc.hackathon.controller;

import jakarta.validation.Valid;
import jdc.hackathon.domain.dto.post.*;
import jdc.hackathon.domain.enumType.District;
import jdc.hackathon.domain.enumType.PostCategory;
import jdc.hackathon.domain.enumType.PostStatus;
import jdc.hackathon.security.CustomUserDetails;
import jdc.hackathon.service.DonationPostService;
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


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class DonationPostController {
    private final DonationPostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @RequestBody @Valid CreatePostRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        Long userId = userDetails.getUser().getId();
        PostResponse res = postService.createPost(userId, req);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long postId,
            @RequestBody @Valid CreatePostRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        Long userId = userDetails.getUser().getId();
        PostResponse res = postService.updatePost(userId, postId, req);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        Long userId = userDetails.getUser().getId();
        postService.deletePost(userId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> get(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping
    public ResponseEntity<Page<PostSummaryResponse>> list(
            @RequestParam(required = false) PostCategory category,
            @RequestParam(required = false) PostStatus status,
            @RequestParam(required = false) District location,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostSummaryResponse> page = postService.listPosts(category, status, location, q, pageable);
        return ResponseEntity.ok(page);
    }
}
