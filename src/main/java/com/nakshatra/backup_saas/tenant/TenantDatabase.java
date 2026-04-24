package com.nakshatra.backup_saas.tenant;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tenant_databases")
@Data
public class TenantDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String dbType;
    private String host;
    private Integer port;
    private String username;
    private String passwordEncrypted;
    private String databaseName;
}