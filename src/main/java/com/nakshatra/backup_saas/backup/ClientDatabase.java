package com.nakshatra.backup_saas.backup;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client_databases")
@Data
public class ClientDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String host;
    private int port;
    private String username;
    private String password;
    private String databaseName;
}