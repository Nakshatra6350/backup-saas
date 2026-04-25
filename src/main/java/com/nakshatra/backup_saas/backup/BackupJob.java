package com.nakshatra.backup_saas.backup;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "backup_jobs")
@Data
public class BackupJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientDbId;

    private String cronExpression;

    private boolean enabled;

    private LocalDateTime lastRun;
    private LocalDateTime nextRun;
}
