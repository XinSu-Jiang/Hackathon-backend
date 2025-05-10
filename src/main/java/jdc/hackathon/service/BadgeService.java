package jdc.hackathon.service;

import jakarta.persistence.EntityNotFoundException;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.enumType.BadgeType;
import jdc.hackathon.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final UserRepository userRepo;

    public void checkAndAward(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        int pts = user.getDeokPoints();

        if (pts >= 10 && !user.hasBadge(BadgeType.DEOK_10)) {
            user.awardBadge(BadgeType.DEOK_10);
        }
        if (pts >= 100 && !user.hasBadge(BadgeType.DEOK_100)) {
            user.awardBadge(BadgeType.DEOK_100);
        }
        if (pts >= 500 && !user.hasBadge(BadgeType.DEOK_500)) {
            user.awardBadge(BadgeType.DEOK_500);
        }

        userRepo.save(user);
    }
}
