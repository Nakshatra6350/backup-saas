package com.nakshatra.backup_saas.config;

import com.nakshatra.backup_saas.common.context.TenantContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private final DataSource masterDataSource;

    // 🔥 GLOBAL CACHE
    private final Map<Object, DataSource> tenantDataSources = new HashMap<>();

    public DataSourceConfig(@Qualifier("masterDataSource") DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    // 🔥 used by initializer
    public void addTenantDataSource(Long tenantId, DataSource ds) {
        tenantDataSources.put(tenantId, ds);
    }

    @Bean
    @Primary
    public DataSource routingDataSource() {

        AbstractRoutingDataSource routing = new AbstractRoutingDataSource() {

            @Override
            protected Object determineCurrentLookupKey() {
                return TenantContext.getTenantId();
            }

            @Override
            protected DataSource determineTargetDataSource() {

                Long tenantId = TenantContext.getTenantId();

                if (tenantId == null) {
                    return masterDataSource;
                }

                DataSource ds = tenantDataSources.get(tenantId);

                if (ds == null) {
                    throw new RuntimeException("Tenant datasource not initialized");
                }

                return ds;
            }
        };

        Map<Object, Object> map = new HashMap<>();
        map.put("MASTER", masterDataSource);

        routing.setTargetDataSources(map);
        routing.setDefaultTargetDataSource(masterDataSource);
        routing.afterPropertiesSet();

        return routing;
    }
}