package com.nakshatra.backup_saas.config;

import com.nakshatra.backup_saas.common.context.TenantContext;
import com.nakshatra.backup_saas.tenant.TenantDatabase;
import com.nakshatra.backup_saas.tenant.TenantDatabaseRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {


    private final JdbcTemplate jdbcTemplate;

    public DataSourceConfig(@Qualifier("masterJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Bean(name = "routingDataSource")
    @Primary
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource) {

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("MASTER", masterDataSource);

        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {

            private final Map<Object, DataSource> tenantDataSources = new HashMap<>();

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

                if (!tenantDataSources.containsKey(tenantId)) {

                    try (Connection conn = masterDataSource.getConnection();
                         PreparedStatement ps = conn.prepareStatement(
                                 "SELECT * FROM tenant_databases WHERE tenant_id = ?")) {

                        ps.setLong(1, tenantId);
                        ResultSet rs = ps.executeQuery();

                        if (!rs.next()) {
                            throw new RuntimeException("DB config not found");
                        }

                        String url = "jdbc:mysql://" + rs.getString("host") + ":" + rs.getInt("port")
                                + "/" + rs.getString("database_name");

                        DataSource ds = DataSourceBuilder.create()
                                .url(url)
                                .username(rs.getString("username"))
                                .password(rs.getString("password_encrypted"))
                                .driverClassName("com.mysql.cj.jdbc.Driver")
                                .build();

                        tenantDataSources.put(tenantId, ds);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                return tenantDataSources.get(tenantId);
            }
        };

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }
}
