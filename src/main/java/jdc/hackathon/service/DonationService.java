package jdc.hackathon.service;

import jdc.hackathon.domain.dto.donation.*;
import jdc.hackathon.domain.entity.*;
import jdc.hackathon.domain.enumType.DonationStatus;
import jdc.hackathon.domain.repository.*;
import jdc.hackathon.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepo;
    private final UserRepository userRepo;
    private final DonationPostRepository postRepo;

    @Transactional
    public DonationResponse pledge(Long userId, Long postId, CreateDonationRequest req) {
        User donor = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        DonationPost post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("포스트가 없습니다."));

        Donation d = Donation.builder()
                .donor(donor)
                .post(post)
                .amount(req.getAmount())
                .status(DonationStatus.PLEDGED)
                .pledgedAt(LocalDateTime.now())
                .build();

        donationRepo.save(d);
        return toResponse(d);
    }

    @Transactional(readOnly = true)
    public List<DonationResponse> getMyDonations(Long userId) {
        return donationRepo.findAllByDonorId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonationResponse> getPostDonations(Long userId, Long postId) {
        // 검증: 이 포스트의 작성자가 userId가 맞는지 체크
        DonationPost post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("포스트가 없습니다."));
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        return donationRepo.findAllByPostId(postId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private DonationResponse toResponse(Donation d) {
        return DonationResponse.builder()
                .id(d.getId())
                .donor(new DonationResponse.SimpleUser(
                        d.getDonor().getId(), d.getDonor().getNickname(), d.getDonor().getProfileImage()))
                .postId(d.getPost().getId())
                .amount(d.getAmount())
                .status(d.getStatus())
                .pledgedAt(d.getPledgedAt())
                .completedAt(d.getCompletedAt())
                .build();
    }

    /**
     * 3분마다 실행: donationDate 지난 '약정(PLEDGED)' 건 → VALIDATE & 머니 차감 → COMPLETED
     */
    @Scheduled(fixedDelay = 180_000)
    @Transactional
    public void completeScheduledDonations() {
        LocalDateTime now = LocalDateTime.now();
        donationRepo.findAllReadyToComplete(now).forEach(d -> {
            User donor = d.getDonor();
            if (donor.getSeedMoneyBalance() < d.getAmount()) {
                d.setStatus(DonationStatus.REFUNDED);
                return;
            }
            donor.setSeedMoneyBalance(donor.getSeedMoneyBalance() - d.getAmount());
            d.complete();
        });
    }
}
