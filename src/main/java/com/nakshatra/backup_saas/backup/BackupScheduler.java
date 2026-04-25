package com.nakshatra.backup_saas.backup;

import com.nakshatra.backup_saas.common.context.TenantContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BackupScheduler {

    private final BackupJobRepository jobRepo;
    private final JdbcTemplate masterJdbcTemplate;
    private final BackupQueueService queueService;

    public BackupScheduler(
            BackupJobRepository jobRepo,
            @Qualifier("masterJdbcTemplate") JdbcTemplate masterJdbcTemplate, BackupQueueService queueService) {

        this.jobRepo = jobRepo;
        this.masterJdbcTemplate = masterJdbcTemplate;
        this.queueService = queueService;
    }

    @Scheduled(fixedDelay = 60000)
    public void runScheduledBackups() {

        List<Long> tenantIds = masterJdbcTemplate.queryForList(
                "SELECT id FROM tenants",
                Long.class
        );

        for (Long tenantId : tenantIds) {

            try {
                TenantContext.setTenantId(tenantId);

                List<BackupJob> jobs = jobRepo.findByEnabledTrue();

                for (BackupJob job : jobs) {

                    queueService.enqueue(job.getClientDbId());

                    job.setLastRun(LocalDateTime.now());
                    jobRepo.save(job);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                TenantContext.clear();
            }
        }
    }
}
