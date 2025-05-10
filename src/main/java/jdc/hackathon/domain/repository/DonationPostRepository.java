package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.DonationPost;
import jdc.hackathon.domain.enumType.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationPostRepository extends JpaRepository<DonationPost, Long>, JpaSpecificationExecutor<DonationPost> {
    // 모집 상태가 아직 RECRUITING 이고, currentPersonCount >= capacity 인 것들 → FULL 로 전환
    @Query("""
      select p 
      from DonationPost p 
      where p.status = :status 
        and p.currentPersonCount >= p.capacity
    """)
    List<DonationPost> findAllFullCandidates(@Param("status") PostStatus status);

    // 모집 상태가 아직 RECRUITING 이고, 모집 종료일이 지난 것들 → CLOSED 로 전환
    @Query("""
      select p 
      from DonationPost p 
      where p.status = :status 
        and p.donationDate < :now
    """)
    List<DonationPost> findAllExpiredCandidates(
            @Param("status") PostStatus status,
            @Param("now") LocalDateTime now
    );

    Page<DonationPost> findAllByUserId(Long userId, Pageable pageable);
    List<DonationPost> findAllByUserId(Long userId);


}