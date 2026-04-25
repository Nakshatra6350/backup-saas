package com.nakshatra.backup_saas.backup.controller;

import com.nakshatra.backup_saas.backup.service.BackupExecutionService;
import com.nakshatra.backup_saas.common.response.ApiResponse;
import com.nakshatra.backup_saas.common.response.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backup")
public class BackupController {

    private final BackupExecutionService backupExecutionService;

    public BackupController(BackupExecutionService backupExecutionService) {
        this.backupExecutionService = backupExecutionService;
    }

    @PostMapping("/run/{clientDbId}")
    public ApiResponse<String> runBackup(@PathVariable Long clientDbId) {

        // 🔥 TenantContext already set via JWT filter

        backupExecutionService.backupDatabase(clientDbId);

        return ResponseUtil.success("Backup started");
    }

}
