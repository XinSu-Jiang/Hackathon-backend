package jdc.hackathon.service;

import jdc.hackathon.domain.enumType.PostStatus;
import jdc.hackathon.domain.repository.DonationPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostStatusScheduler {

    private final DonationPostRepository postRepo;

    /**
     * 3분마다 모집 상태 갱신:
     * - capacity(정원) 도달 시 → FULL
     * - recruitmentEnd(모집 마감일) 지남 → CLOSED
     */
    @Scheduled(fixedDelay = 180_000)
    @Transactional
    public void refreshStatuses() {

        postRepo.findAllFullCandidates(PostStatus.RECRUITING)
                .forEach(p -> p.setStatus(PostStatus.FULL));

        postRepo.findAllExpiredCandidates(PostStatus.RECRUITING, LocalDateTime.now())
                .forEach(p -> p.setStatus(PostStatus.CLOSED));
    }
}
