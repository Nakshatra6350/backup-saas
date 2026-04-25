package com.nakshatra.backup_saas.backup.repository;

import com.nakshatra.backup_saas.backup.entity.ClientDatabase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDatabaseRepository extends JpaRepository<ClientDatabase, Long> {
}