CREATE TABLE client_databases (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),

    host VARCHAR(100),
    port INT,
    username VARCHAR(100),
    password VARCHAR(255),
    database_name VARCHAR(100),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 1. Add new column
ALTER TABLE backup_history
ADD COLUMN client_db_id BIGINT;

-- 2. (Optional but recommended) migrate old data
-- assuming db_id was pointing to same concept
UPDATE backup_history
SET client_db_id = db_id;

-- 3. Drop old columns
ALTER TABLE backup_history
DROP COLUMN tenant_id,
DROP COLUMN db_id;