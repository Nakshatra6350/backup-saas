package com.nakshatra.backup_saas.storage;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LocalStorageService implements StorageService {

    @Override
    public String store(File file) {
        return file.getAbsolutePath();
    }
}