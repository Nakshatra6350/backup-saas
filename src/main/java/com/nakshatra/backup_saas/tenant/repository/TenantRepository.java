package com.nakshatra.backup_saas.tenant.repository;

import com.nakshatra.backup_saas.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByIdentifier(String identifier);
}