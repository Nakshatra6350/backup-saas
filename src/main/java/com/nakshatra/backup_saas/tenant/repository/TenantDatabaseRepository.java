package com.nakshatra.backup_saas.tenant.repository;

import com.nakshatra.backup_saas.tenant.entity.TenantDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantDatabaseRepository extends JpaRepository<TenantDatabase, Long> {

    Optional<TenantDatabase> findByTenantId(Long tenantId);
}