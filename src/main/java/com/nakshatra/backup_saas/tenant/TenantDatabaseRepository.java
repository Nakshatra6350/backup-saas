package com.nakshatra.backup_saas.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantDatabaseRepository extends JpaRepository<TenantDatabase, Long> {

    Optional<TenantDatabase> findByTenantId(Long tenantId);
}