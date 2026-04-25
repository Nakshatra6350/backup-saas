package com.nakshatra.backup_saas.backup.worker;

import com.nakshatra.backup_saas.backup.queue.BackupQueueService;
import com.nakshatra.backup_saas.backup.service.BackupExecutionService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BackupWorker {

    private final BackupQueueService queueService;
    private final BackupExecutionService backupExecutionService;

    public BackupWorker(BackupQueueService queueService,
                        BackupExecutionService backupExecutionService) {
        this.queueService = queueService;
        this.backupExecutionService = backupExecutionService;
    }

    @PostConstruct
    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Long clientDbId = queueService.dequeue();
                    backupExecutionService.backupDatabase(clientDbId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
