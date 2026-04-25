package com.nakshatra.backup_saas.backup.repository;

import com.nakshatra.backup_saas.backup.entity.BackupHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
}
