package com.nakshatra.backup_saas.storage;

import com.nakshatra.backup_saas.config.TenantConfigService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
public class S3StorageService implements StorageService {

    private final S3Client s3Client;

    private final TenantConfigService configService;



    public S3StorageService(S3Client s3Client,
                            TenantConfigService configService) {
        this.s3Client = s3Client;
        this.configService = configService;
    }

    @Override
    public String store(File file) {

        String accessKey = configService.get("aws", "accessKey");
        String secretKey = configService.get("aws", "secretKey");
        String regionStr = configService.get("aws", "region");
        String bucketName = configService.get("aws", "bucket");

        String bucket = configService.get("aws", "bucket");

        if (accessKey == null || secretKey == null) {
            throw new RuntimeException("AWS credentials not configured for current tenant.");
        }

        try (S3Client s3Client = S3Client.builder()
                .region(Region.of(regionStr))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build()){
            String key = "backups/" + file.getName();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.putObject(request, file.toPath());

            System.out.println("Uploaded " + file.getName() + " to bucket: " + bucketName);

            return key;
        }


    }
}