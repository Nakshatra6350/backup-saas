package com.nakshatra.backup_saas.backup.executor;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class BackupExecutor {

    public String executeMySqlBackup(
            String host,
            int port,
            String username,
            String password,
            String databaseName,
            String outputPath) throws Exception {

        String command = String.format(
                "mysqldump -h %s -P %d -u %s -p\"%s\" %s -r %s",
                host, port, username, password, databaseName, outputPath
        );

        Process process = Runtime.getRuntime().exec(command);

        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        String line;
        while ((line = errorReader.readLine()) != null) {
            System.out.println("ERROR: " + line);
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Backup failed with exit code: " + exitCode);
        }

        return outputPath;
    }
}
