package org.example;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
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
    private static final int PBKDF2_ITERATION_COUNT = 65536;

    /**
     * Generates a random byte array of the specified length.
     * This method is used to generate PBKDF2 salt and AES-GCM nonce.
     *
     * @param length the length of the byte array
     * @return the generated random byte array
     */
    private static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        SecureRandom secureRandom = new SecureRandom();

        secureRandom.nextBytes(bytes);

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
     * Encrypts the text using AES-GCM with the provided password.
     *
     * @param text the text to be encrypted
     * @param password the password
     * @return the encrypted text
     */
    public static String encrypt(String text, String password) {
        try {
            byte[] salt = generateRandomBytes(PBKDF2_SALT_LENGTH);
            byte[] nonce = generateRandomBytes(AES_NONCE_LENGTH);

            // Generate the key from the password and salt, and encrypt the text.
            SecretKey key = generateKeyFromPassword(password, salt);
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM_NAME);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] ciphertext = cipher.doFinal(text.getBytes());

            // Combine salt, nonce, and ciphertext.
            // combined = salt + nonce + ciphertext
            byte[] combined = new byte[PBKDF2_SALT_LENGTH + AES_NONCE_LENGTH + ciphertext.length];
            System.arraycopy(salt, 0, combined, 0, PBKDF2_SALT_LENGTH);
            System.arraycopy(nonce, 0, combined, PBKDF2_SALT_LENGTH, AES_NONCE_LENGTH);
            System.arraycopy(ciphertext, 0, combined, PBKDF2_SALT_LENGTH + AES_NONCE_LENGTH, ciphertext.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt text.", e);
        }
    }

    /**
     * Decrypts the encrypted text using AES-GCM with the provided password.
     *
     * @param encryptedText the encrypted text
     * @param password the password
     * @return the decrypted text
     */
    public static String decrypt(String encryptedText, String password) {
        try {
            byte[] decodedEncryptedText = Base64.getDecoder().decode(encryptedText);

            // Extract salt, nonce, and ciphertext from the decoded encrypted text.
            // decodedEncryptedText = salt + nonce + ciphertext
            byte[] salt = new byte[PBKDF2_SALT_LENGTH];
            byte[] nonce = new byte[AES_NONCE_LENGTH];
            byte[] ciphertext = new byte[decodedEncryptedText.length - PBKDF2_SALT_LENGTH - AES_NONCE_LENGTH];
            System.arraycopy(decodedEncryptedText, 0, salt, 0, PBKDF2_SALT_LENGTH);
            System.arraycopy(decodedEncryptedText, PBKDF2_SALT_LENGTH, nonce, 0, AES_NONCE_LENGTH);
            System.arraycopy(decodedEncryptedText, PBKDF2_SALT_LENGTH + AES_NONCE_LENGTH, ciphertext, 0, ciphertext.length);

            // Generate the secret key from the password and salt, and decrypt the ciphertext.
            SecretKey key = generateKeyFromPassword(password, salt);
            Cipher cipher = Cipher.getInstance(AES_GCM_ALGORITHM_NAME);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] decryptedText = cipher.doFinal(ciphertext);

            return new String(decryptedText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt text.", e);
        }
    }

    public static void main(String[] args) {
        String text = "Hello world! This is a secret message. If you can read this, you are awesome!"
        + "Here are a bunch of random characters: !@#$%^&*()_+" +
                "1234567890-=[]\\;',./`~ This is the end of the message. Even more random characters: {}|:\"<>?";
        String password = "1a2b3c4d5f";

        System.out.println("Original text: " + text);

        String encryptedText = encrypt(text, password);
        System.out.println("Encrypted text: " + encryptedText);

        String decryptedText = decrypt(encryptedText, password);
        System.out.println("Decrypted text: " + decryptedText);

        String failedDecryptedText = decrypt(encryptedText, "wrongpassword");
        System.out.println("Failed decrypted text: " + failedDecryptedText);
    }
}
