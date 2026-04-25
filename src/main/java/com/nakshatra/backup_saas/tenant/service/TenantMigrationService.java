package com.nakshatra.backup_saas.tenant.service;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class TenantMigrationService {

    public void migrate(DataSource ds) {

        Flyway flyway = Flyway.configure()
                .dataSource(ds)
                .locations("classpath:db/tenant_migration")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }
}