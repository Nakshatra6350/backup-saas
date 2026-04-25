package com.nakshatra.backup_saas.config;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "external_properties_configurations")
@Data
public class ExternalConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String propertyKey;
    private String propertyValue;
    private boolean active;
}
