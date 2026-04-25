package com.nakshatra.backup_saas.tenant.repository;

import com.nakshatra.backup_saas.tenant.entity.ExternalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExternalConfigRepository
        extends JpaRepository<ExternalConfig, Long> {

    List<ExternalConfig> findByServiceNameAndActiveTrue(String serviceName);

    Optional<ExternalConfig> findByServiceNameAndPropertyKeyAndActiveTrue(String serviceName, String propertyKey);
}
