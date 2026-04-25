CREATE TABLE backup_jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    client_db_id BIGINT,

    cron_expression VARCHAR(100),

    enabled BOOLEAN DEFAULT TRUE,

    last_run TIMESTAMP,
    next_run TIMESTAMP
);