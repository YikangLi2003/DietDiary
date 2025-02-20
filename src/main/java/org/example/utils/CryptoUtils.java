package org.example.utils;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.MessageDigest;

/**
 * This class provides utility methods for encrypting and decrypting data using no-padding AES-GCM algorithm.
 */
public class CryptoUtils {
    private static final String AES_GCM_ALGORITHM_NAME = "AES/GCM/NoPadding";
    private static final String AES_ALGORITHM_NAME = "AES";
    private static final String SHA_ALGORITHM_NAME = "SHA-256";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int AES_IV_LENGTH = 12;

    /**
     * Generates a secret key for AES encryption using SHA-256 with the provided password.
     *
     * @param password the password to generate the key from
     * @return the secret key
     */
    private static SecretKey generateKeyFromPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_ALGORITHM_NAME);
            byte[] hash = digest.digest(password.getBytes());

            return new SecretKeySpec(hash, AES_ALGORITHM_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate key for AES from user's password.", e);
        }
    }

    /**
     * Encrypts the provided data using AES-GCM with the provided password.
     *
     * @param plainData the data to encrypt
     * @param password the password to encrypt the data with
     * @return the encrypted data
     */
    public static byte[] encrypt(byte[] plainData, String password) {
        try {
            byte[] iv = new byte[AES_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            SecretKey key = generateKeyFromPassword(password);
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM_NAME);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] encryptedData = cipher.doFinal(plainData);

            byte[] encryptedDataWithIV = new byte[AES_IV_LENGTH + encryptedData.length];
            System.arraycopy(iv, 0, encryptedDataWithIV, 0, AES_IV_LENGTH);
            System.arraycopy(encryptedData, 0, encryptedDataWithIV, AES_IV_LENGTH, encryptedData.length);

            return encryptedDataWithIV;
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data.", e);
        }
    }

    /**
     * Decrypts the provided data using AES-GCM with the provided password.
     *
     * @param encryptedDataWithIV the data to decrypt
     * @param password the password to decrypt the data with
     * @return the decrypted data
     * @throws AEADBadTagException if the password is incorrect or the data is tampered
     */
    public static byte[] decrypt(byte[] encryptedDataWithIV, String password) throws AEADBadTagException {
        try {
            byte[] iv = new byte[AES_IV_LENGTH];
            System.arraycopy(encryptedDataWithIV, 0, iv, 0, AES_IV_LENGTH);

            byte[] encryptedData = new byte[encryptedDataWithIV.length - AES_IV_LENGTH];
            System.arraycopy(encryptedDataWithIV, AES_IV_LENGTH, encryptedData, 0, encryptedData.length);

            SecretKey key = generateKeyFromPassword(password);
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM_NAME);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            return cipher.doFinal(encryptedData);
        } catch (AEADBadTagException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt content.", e);
        }
    }
}
