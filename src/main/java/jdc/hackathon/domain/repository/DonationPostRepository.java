package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.DonationPost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationPostRepository extends JpaRepository<DonationPost, Long>, JpaSpecificationExecutor<DonationPost> {
}