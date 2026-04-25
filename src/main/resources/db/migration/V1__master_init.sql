CREATE TABLE tenants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    identifier VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tenant_databases (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT,
    db_type VARCHAR(50),
    host VARCHAR(100),
    port INT,
    username VARCHAR(100),
    password_encrypted TEXT,
    database_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);