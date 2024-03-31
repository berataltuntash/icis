package com.icis.demo.Utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    public static String encryptPassword(String text) throws Exception {
        String key = generateKey();
        byte[] decodedKey = Base64.getDecoder().decode(key);

        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);

        byte[] textBytes = text.getBytes();
        byte[] encryptedBytes = cipher.doFinal(textBytes);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String generateKey() throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        SecretKey key = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
