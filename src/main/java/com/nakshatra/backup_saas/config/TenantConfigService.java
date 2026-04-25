package com.nakshatra.backup_saas.config;

import com.nakshatra.backup_saas.tenant.entity.ExternalConfig;
import com.nakshatra.backup_saas.tenant.repository.ExternalConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantConfigService {

    private final ExternalConfigRepository repo;

//    private final Map<String, Map<String, String>> cache = new HashMap<>();

    public TenantConfigService(ExternalConfigRepository repo) {
        this.repo = repo;
    }

    public String get(String service, String key) {

        // 🔥 This will run AFTER tenant context is set
        Optional<ExternalConfig> config = repo.findByServiceNameAndPropertyKeyAndActiveTrue(service, key);

        return config
                .map(ExternalConfig::getPropertyValue)
                .orElse(null);
    }
}
