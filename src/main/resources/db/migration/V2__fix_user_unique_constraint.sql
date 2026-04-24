ALTER TABLE users DROP INDEX email;
ALTER TABLE users ADD UNIQUE (email, tenant_id);