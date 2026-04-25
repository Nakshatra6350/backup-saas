package com.nakshatra.backup_saas.backup;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "backup_history")
@Data
public class BackupHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientDbId;

    private String filePath;
    private String status;

    private Long fileSize;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private Integer retryCount = 0;

    private String errorMessage;
}
