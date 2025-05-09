package jdc.hackathon.service;

import jakarta.persistence.EntityNotFoundException;
import jdc.hackathon.domain.dto.post.*;
import jdc.hackathon.domain.entity.DonationPost;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.enumType.District;
import jdc.hackathon.domain.enumType.PostCategory;
import jdc.hackathon.domain.enumType.PostStatus;
import jdc.hackathon.domain.repository.DonationPostRepository;
import jdc.hackathon.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DonationPostServiceImpl implements DonationPostService {

    private final DonationPostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public PostResponse createPost(Long userId, CreatePostRequest req) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        DonationPost post = DonationPost.builder()
                .user(currentUser)
                .category(req.getCategory())
                .title(req.getTitle())
                .description(req.getDescription())
                .location(req.getLocation())
                .donationDate(req.getDonationDate())
                .capacity(req.getCapacity())
                .currentPersonCount(0)
                .isDonationOpen(req.getIsDonationOpen())
                .maxAmount(req.getMaxAmount())
                .currentFundingAmount(0)
                .status(PostStatus.RECRUITING)
                .build();

        DonationPost saved = postRepository.save(post);
        return mapToResponse(saved);
    }

    @Transactional
    @Override
    public PostResponse updatePost(Long userId, Long postId, CreatePostRequest req) {
        DonationPost post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }
        if (req.getCategory() != null) post.setCategory(req.getCategory());
        if (StringUtils.hasText(req.getTitle())) post.setTitle(req.getTitle());
        if (req.getDescription() != null) post.setDescription(req.getDescription());
        if (req.getLocation() != null) post.setLocation(req.getLocation());
        if (req.getDonationDate() != null) post.setDonationDate(req.getDonationDate());
        if (req.getCapacity() != null) post.setCapacity(req.getCapacity());
        if (req.getIsDonationOpen() != null) post.setIsDonationOpen(req.getIsDonationOpen());
        if (req.getMaxAmount() != null) post.setMaxAmount(req.getMaxAmount());

        return mapToResponse(post);
    }

    @Transactional
    @Override
    public void deletePost(Long userId, Long postId) {
        DonationPost post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }
        postRepository.deleteById(postId);
    }

    @Override
    public PostResponse getPost(Long postId) {
        DonationPost post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        return mapToResponse(post);
    }

    @Override
    public Page<PostSummaryResponse> listPosts(PostCategory category, PostStatus status, District location, String q, Pageable pageable) {
        Specification<DonationPost> spec = Specification.where(null);
        if (category != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        if (status != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        if (location != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("location"), location));
        if (StringUtils.hasText(q)) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(root.get("title"), "%" + q + "%"),
                    cb.like(root.get("description"), "%" + q + "%")
            ));
        }
        return postRepository.findAll(spec, pageable).map(this::mapToSummary);
    }

    private PostResponse mapToResponse(DonationPost post) {
        return PostResponse.builder()
                .id(post.getId())
                .author(new PostResponse.UserSummary(post.getUser().getId(), post.getUser().getNickname(), post.getUser().getProfileImage()))
                .category(post.getCategory())
                .title(post.getTitle())
                .description(post.getDescription())
                .location(post.getLocation())
                .donationDate(post.getDonationDate())
                .capacity(post.getCapacity())
                .currentPersonCount(post.getApplications().size())
                .isDonationOpen(post.getIsDonationOpen())
                .maxAmount(post.getMaxAmount())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private PostSummaryResponse mapToSummary(DonationPost post) {
        return PostSummaryResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .location(post.getLocation())
                .category(post.getCategory())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
