package com.nakshatra.backup_saas.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MasterDataSourceConfig {

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/backup_saas_master")
                .username("root")
                .password("root")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean
    public JdbcTemplate masterJdbcTemplate(@Qualifier("masterDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}