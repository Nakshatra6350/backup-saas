package com.nakshatra.backup_saas.storage;

import com.nakshatra.backup_saas.config.ExternalConfigService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
public class S3StorageService implements StorageService {

    private final S3Client s3Client;

    private final ExternalConfigService configService;



    public S3StorageService(S3Client s3Client,
                            ExternalConfigService configService) {
        this.s3Client = s3Client;
        this.configService = configService;
    }

    @Override
    public String store(File file) {

        String bucket = configService.get("aws", "bucket");

        String key = "backups/" + file.getName();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.putObject(request, file.toPath());

        return key;
    }
}