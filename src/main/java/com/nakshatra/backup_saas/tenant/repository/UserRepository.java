package com.nakshatra.backup_saas.tenant.repository;

import com.nakshatra.backup_saas.tenant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
