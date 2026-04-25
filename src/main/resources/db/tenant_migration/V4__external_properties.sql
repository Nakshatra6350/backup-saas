CREATE TABLE external_properties_configurations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    service_name VARCHAR(100),
    property_key VARCHAR(100),
    property_value TEXT,

    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);