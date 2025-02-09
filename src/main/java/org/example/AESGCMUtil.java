package org.example;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;

/**
 * AES-GCM encryption and decryption utility.
 */
public class AESGCMUtil {
    private static final String AES_GCM_ALGORITHM_NAME = "AES/GCM/NoPadding";
    private static final String AES_ALGORITHM_NAME = "AES";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int AES_NONCE_LENGTH = 12;
    private static final int AES_KEY_LENGTH = 256;

    private static final String PBKDF2_ALGORITHM_NAME = "PBKDF2WithHmacSHA256";
    private static final int PBKDF2_SALT_LENGTH = 16;
    private static final int PBKDF2_ITERATION_COUNT = 100000;

    /**
     * Generates a random byte array of the specified length.
     * This method is used to generate PBKDF2 salt and AES-GCM nonce.
     *
     * @param length the length of the byte array i.e. number of bytes in the array
     * @return the generated random byte array
     */
    private static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);

        return bytes;
    }

    /**
     * Generates a secret key for AES encryption/decryption from the user's password and salt.
     *
     * @param password the user's password
     * @param salt the salt
     * @return the generated secret key
     */
    private static SecretKey generateKeyFromPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATION_COUNT, AES_KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM_NAME);
            byte[] keyBytes = keyFactory.generateSecret(spec).getEncoded();

            return new SecretKeySpec(keyBytes, AES_ALGORITHM_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate key for AES from user's password.", e);
        }
    }

    /**
     * Encrypts your data using AES-GCM with the provided password.
     *
     * @param plainContent bytes of the data to be encrypted (you need to convert your data structure to bytes)
     * @param password the password
     * @return the encrypted bytes
     */
    public static byte[] encrypt(byte[] plainContent, String password) {
        try {
            byte[] salt = generateRandomBytes(PBKDF2_SALT_LENGTH);
            byte[] nonce = generateRandomBytes(AES_NONCE_LENGTH);

            // Generate the key from the password and salt, and encrypt the text.
            SecretKey key = generateKeyFromPassword(password, salt);
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM_NAME);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] encryptedContent = cipher.doFinal(plainContent);

            // Combine salt, nonce, and encrypted content.
            // combined = salt + nonce + encryptedContent
            byte[] combined = new byte[PBKDF2_SALT_LENGTH + AES_NONCE_LENGTH + encryptedContent.length];
            System.arraycopy(salt, 0, combined, 0, PBKDF2_SALT_LENGTH);
            System.arraycopy(nonce, 0, combined, PBKDF2_SALT_LENGTH, AES_NONCE_LENGTH);
            System.arraycopy(
                    encryptedContent,
                    0,
                    combined,
                    PBKDF2_SALT_LENGTH + AES_NONCE_LENGTH,
                    encryptedContent.length
            );

            return combined;
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt text.", e);
        }
    }

    /**
     * Decrypts the encrypted text using AES-GCM with the provided password.
     *
     * @param encryptedContent the encrypted content to be decrypted with the format salt + nonce + cipher content
     * @param password the password
     * @return the decrypted bytes (you need to convert it back to your data structure)
     */
    public static byte[] decrypt(byte[] encryptedContent, String password) throws AEADBadTagException {
        try {
            // Extract salt, nonce, and content from the encryptedContent.
            // encryptedContent = salt + nonce + content
            byte[] salt = new byte[PBKDF2_SALT_LENGTH];
            byte[] nonce = new byte[AES_NONCE_LENGTH];
            byte[] content = new byte[encryptedContent.length - PBKDF2_SALT_LENGTH - AES_NONCE_LENGTH];
            System.arraycopy(encryptedContent, 0, salt, 0, PBKDF2_SALT_LENGTH);
            System.arraycopy(encryptedContent, PBKDF2_SALT_LENGTH, nonce, 0, AES_NONCE_LENGTH);
            System.arraycopy(
                    encryptedContent,
                    PBKDF2_SALT_LENGTH + AES_NONCE_LENGTH,
                    content,
                    0,
                    content.length
            );

            // Generate the secret key from the password and salt, and decrypt the content.
            SecretKey key = generateKeyFromPassword(password, salt);
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM_NAME);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            return cipher.doFinal(content);
        } catch (AEADBadTagException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt content.", e);
        }
    }
}
