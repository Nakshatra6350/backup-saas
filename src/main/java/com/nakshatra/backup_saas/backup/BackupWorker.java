package com.nakshatra.backup_saas.backup;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BackupWorker {

    private final BackupQueueService queueService;
    private final BackupService backupService;

    public BackupWorker(BackupQueueService queueService,
                        BackupService backupService) {
        this.queueService = queueService;
        this.backupService = backupService;
    }

    @PostConstruct
    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Long clientDbId = queueService.dequeue();
                    backupService.backupDatabase(clientDbId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
