-- tenants
CREATE TABLE tenants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    identifier VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- users
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- tenant databases
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

-- backup jobs
CREATE TABLE backup_jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT,
    db_id BIGINT,
    cron_expression VARCHAR(100),
    backup_type VARCHAR(50),
    storage_type VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    FOREIGN KEY (db_id) REFERENCES tenant_databases(id)
);

-- backup history
CREATE TABLE backup_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT,
    db_id BIGINT,
    file_path TEXT,
    status VARCHAR(50),
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    file_size BIGINT
);

-- restore history
CREATE TABLE restore_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    backup_id BIGINT,
    status VARCHAR(50),
    started_at TIMESTAMP,
    completed_at TIMESTAMP
);

-- audit logs
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT,
    user_id BIGINT,
    api VARCHAR(255),
    method VARCHAR(10),
    request_body TEXT,
    response_body TEXT,
    status VARCHAR(50),
    execution_time BIGINT,
    trace_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- subscriptions
CREATE TABLE subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT,
    plan VARCHAR(50),
    status VARCHAR(50),
    expiry TIMESTAMP
);