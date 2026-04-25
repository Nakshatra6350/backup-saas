package com.nakshatra.backup_saas.backup.repository;

import com.nakshatra.backup_saas.backup.entity.BackupJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackupJobRepository extends JpaRepository<BackupJob, Long> {
    List<BackupJob> findByEnabledTrue();
}
