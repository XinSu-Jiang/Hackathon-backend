package jdc.hackathon.domain.repository;

import jdc.hackathon.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
