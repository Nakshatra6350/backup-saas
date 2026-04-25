package com.nakshatra.backup_saas.tenant.service;

import com.nakshatra.backup_saas.config.TenantRoutingDataSourceConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.*;

@Component
public class TenantInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final TenantMigrationService migrationService;
    private final TenantRoutingDataSourceConfig tenantRoutingDataSourceConfig;

    public TenantInitializer(
            JdbcTemplate jdbcTemplate,
            TenantMigrationService migrationService,
            TenantRoutingDataSourceConfig tenantRoutingDataSourceConfig) {

        this.jdbcTemplate = jdbcTemplate;
        this.migrationService = migrationService;
        this.tenantRoutingDataSourceConfig = tenantRoutingDataSourceConfig;
    }

    @PostConstruct
    public void init() {

        List<Map<String, Object>> tenants =
                jdbcTemplate.queryForList("SELECT * FROM tenant_databases");

        for (Map<String, Object> db : tenants) {

            Long tenantId = ((Number) db.get("tenant_id")).longValue();

            String url = "jdbc:mysql://" + db.get("host") + ":" + db.get("port")
                    + "/" + db.get("database_name");

            DataSource ds = DataSourceBuilder.create()
                    .url(url)
                    .username((String) db.get("username"))
                    .password((String) db.get("password_encrypted"))
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();

            System.out.println("Migrating tenant: " + tenantId);

            // 🔥 run tenant migration
            migrationService.migrate(ds);

            // 🔥 cache datasource
            tenantRoutingDataSourceConfig.addTenantDataSource(tenantId, ds);
        }

        System.out.println("✅ All tenant DBs initialized");
    }
}