package com.nakshatra.backup_saas.backup;

import com.nakshatra.backup_saas.common.context.TenantContext;
import com.nakshatra.backup_saas.common.response.ApiResponse;
import com.nakshatra.backup_saas.common.response.ResponseUtil;
import com.nakshatra.backup_saas.tenant.TenantDatabase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/backup")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/run/{clientDbId}")
    public ApiResponse<String> runBackup(@PathVariable Long clientDbId) {

        // 🔥 TenantContext already set via JWT filter

        backupService.backupDatabase(clientDbId);

        return ResponseUtil.success("Backup started");
    }
}
