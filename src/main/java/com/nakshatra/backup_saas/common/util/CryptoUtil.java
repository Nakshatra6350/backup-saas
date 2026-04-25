package com.nakshatra.backup_saas.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

@Component
public class CryptoUtil {

    private final String key;

    public CryptoUtil(@Value("${crypto.key}") String key) {
        this.key = key;
    }

    public String encrypt(String value) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File encryptFile(File inputFile) throws Exception {
        File encryptedFile = new File(inputFile.getPath() + ".enc");

        // Use the key that was injected via the constructor
        SecretKeySpec secretKey = new SecretKeySpec(this.key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(encryptedFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[8192]; // 8KB buffer is more efficient
            int read;

            // CipherOutputStream automatically handles update() and doFinal()
            while ((read = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, read);
            }
        }

        return encryptedFile;
    }
}
