package com.nakshatra.backup_saas.config;

import com.nakshatra.backup_saas.storage.LocalStorageService;
import com.nakshatra.backup_saas.storage.S3StorageService;
import com.nakshatra.backup_saas.storage.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File; // ✅ IMPORTANT

@Configuration
public class StorageConfig {

    @Bean
    public StorageService storageService(
            S3StorageService s3,
            LocalStorageService local,
            TenantConfigService externalConfigService) { // ✅ inject this

        return new StorageService() {

            @Override
            public String store(File file) {

                String enabled = externalConfigService.get("aws", "enabled");

                if ("true".equalsIgnoreCase(enabled)) {
                    return s3.store(file);
                }

                return local.store(file);
            }
        };
    }
}