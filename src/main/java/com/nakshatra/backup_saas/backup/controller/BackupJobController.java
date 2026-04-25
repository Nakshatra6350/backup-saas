package com.nakshatra.backup_saas.backup.controller;

import com.nakshatra.backup_saas.backup.entity.BackupJob;
import com.nakshatra.backup_saas.backup.repository.BackupJobRepository;
import com.nakshatra.backup_saas.common.response.ApiResponse;
import com.nakshatra.backup_saas.common.response.ResponseUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackupJobController {

    private final BackupJobRepository jobRepo;

    public BackupJobController(BackupJobRepository jobRepo) {
        this.jobRepo = jobRepo;
    }

    @PostMapping("/job")
    public ApiResponse<String> createJob(@RequestBody BackupJob job) {

        jobRepo.save(job);

        return ResponseUtil.success("Job created");
    }
}
