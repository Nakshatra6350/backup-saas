package com.nakshatra.backup_saas.backup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDatabaseRepository extends JpaRepository<ClientDatabase, Long> {
}