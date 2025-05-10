package jdc.hackathon.service;

import jdc.hackathon.domain.dto.post.*;
import jdc.hackathon.domain.enumType.District;
import jdc.hackathon.domain.enumType.PostCategory;
import jdc.hackathon.domain.enumType.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonationPostService {
    PostResponse createPost(Long userId, CreatePostRequest request);
    PostResponse updatePost(Long userId, Long postId, CreatePostRequest request);
    void deletePost(Long userId, Long postId);
    PostResponse getPost(Long postId, Long currentUserId);
    Page<PostSummaryResponse> listPosts(PostCategory category, PostStatus status, District location, String q, Pageable pageable);
    Page<PostSummaryResponse> listMyPosts(Long userId, Pageable pageable);
    List<PostResponse> findPostsByUser(Long userId);
}