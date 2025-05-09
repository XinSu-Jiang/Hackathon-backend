package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Integer> {

    @Query("""
      select d
        from Donation d
       where d.status = 'PLEDGED'
         and d.post.donationDate < :now
    """)
    List<Donation> findAllReadyToComplete(@Param("now") LocalDateTime now);

    // 내 약정 목록
    List<Donation> findAllByDonorId(Long donorId);

    // 특정 포스트에 대한 약정 목록
    List<Donation> findAllByPostId(Long postId);
}
