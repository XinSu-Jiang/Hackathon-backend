package jdc.hackathon.service;

import jakarta.persistence.EntityNotFoundException;
import jdc.hackathon.domain.dto.application.ApplicationResponse;
import jdc.hackathon.domain.dto.application.UpdateApplicationRequest;
import jdc.hackathon.domain.entity.Application;
import jdc.hackathon.domain.entity.DonationPost;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.enumType.ApplicationStatus;
import jdc.hackathon.domain.enumType.PostStatus;
import jdc.hackathon.domain.repository.ApplicationRepository;
import jdc.hackathon.domain.repository.DonationPostRepository;
import jdc.hackathon.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final DonationPostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public ApplicationResponse apply(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        DonationPost post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (applicationRepository.findByPostIdAndUserId(postId, userId) != null) {
            throw new IllegalStateException("이미 신청된 글입니다.");
        }
        Application app = Application.builder()
                .post(post)
                .user(user)
                .build();
        Application saved = applicationRepository.save(app);
        return map(saved);
    }

    @Override
    public void cancel(Long userId, Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        if (!app.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }
        if (app.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("처리 후에는 철회할 수 없습니다.");
        }
        applicationRepository.delete(app);
    }

    @Override
    public ApplicationResponse respond(Long userId, Long applicationId, UpdateApplicationRequest req) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        DonationPost post = app.getPost();
        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }
        if (app.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 신청입니다.");
        }
        app.setStatus(req.getStatus());
        app.setRespondedAt(LocalDateTime.now());
        if (req.getStatus() == ApplicationStatus.ACCEPTED) {
            post.setCurrentPersonCount(post.getCurrentPersonCount() + 1);
            if (post.getCurrentPersonCount() >= post.getCapacity()) {
                post.setStatus(PostStatus.FULL);
            }
        }
        return map(app);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(Long userId) {
        return applicationRepository.findAllByUserId(userId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getPostApplications(Long userId, Long postId) {
        DonationPost post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized");
        }
        return applicationRepository.findAllByPostId(postId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    private ApplicationResponse map(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .postId(app.getPost().getId())
                .user(new ApplicationResponse.UserSummary(
                        app.getUser().getId(),
                        app.getUser().getNickname(),
                        app.getUser().getProfileImage()
                ))
                .status(app.getStatus())
                .appliedAt(app.getAppliedAt())
                .respondedAt(app.getRespondedAt())
                .build();
    }
}
