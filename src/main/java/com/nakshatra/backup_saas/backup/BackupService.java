package com.nakshatra.backup_saas.backup;

import com.nakshatra.backup_saas.common.util.CryptoUtil;
import com.nakshatra.backup_saas.storage.StorageService;
import com.nakshatra.backup_saas.tenant.TenantDatabase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Service
public class BackupService {


    private final BackupQueueService queueService;
    private final BackupExecutor executor;
    private final BackupHistoryRepository historyRepository;
    private final ClientDatabaseRepository clientDbRepo;
    private final StorageService storageService;
    private final CryptoUtil cryptoUtil;
    private static final Logger log = LoggerFactory.getLogger(BackupService.class);

    public BackupService(
            BackupQueueService queueService, BackupExecutor executor,
            BackupHistoryRepository historyRepository,
            ClientDatabaseRepository clientDbRepo, StorageService storageService, CryptoUtil cryptoUtil) {
        this.queueService = queueService;

        this.executor = executor;
        this.historyRepository = historyRepository;
        this.clientDbRepo = clientDbRepo;
        this.storageService = storageService;
        this.cryptoUtil = cryptoUtil;
    }

    @Async("backupExecutorPool")
    public void backupDatabase(Long clientDbId) {

        // 🔥 Fetch CLIENT DB from TENANT DB
        ClientDatabase db = clientDbRepo.findById(clientDbId)
                .orElseThrow(() -> new RuntimeException("Client DB not found"));

        BackupHistory history = new BackupHistory();
        history.setClientDbId(db.getId());
        history.setStatus("IN_PROGRESS");
        history.setStartedAt(LocalDateTime.now());

        historyRepository.save(history);

        try {
            String folder = System.getProperty("user.dir") + "/backups/";
            File dir = new File(folder);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = folder + db.getDatabaseName()
                    + "_" + System.currentTimeMillis() + ".sql";

            String decryptedPassword = cryptoUtil.decrypt(db.getPassword());

//            db.setPassword(cryptoUtil.encrypt(password)); // use it where ever needed in future while saving db config

            executor.executeMySqlBackup(
                    db.getHost(),
                    db.getPort(),
                    db.getUsername(),
                    decryptedPassword,
                    db.getDatabaseName(),
                    filePath
            );

            File file = new File(filePath);
            File encrypted = cryptoUtil.encryptFile(file);
            String storedPath = storageService.store(encrypted);
            history.setFilePath(storedPath);
            history.setFileSize(file.length());
            history.setStatus("SUCCESS");

        } catch (Exception e) {

            history.setErrorMessage(e.getMessage());
            history.setRetryCount(
                    history.getRetryCount() == null ? 1 : history.getRetryCount() + 1
            );

            historyRepository.save(history);

            if (history.getRetryCount() < 3) {
                queueService.enqueue(clientDbId);
            } else{
                log.error("Backup failed for DB: {}", db.getDatabaseName(), e);
                throw new RuntimeException("Backup failed", e);
            }


        }

        history.setCompletedAt(LocalDateTime.now());

        historyRepository.save(history);
    }


}
