package com.nakshatra.backup_saas.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExternalConfigService {

    private final ExternalConfigRepository repo;

//    private final Map<String, Map<String, String>> cache = new HashMap<>();

    public ExternalConfigService(ExternalConfigRepository repo) {
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
