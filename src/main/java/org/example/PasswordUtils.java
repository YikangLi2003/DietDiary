package org.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt password hashing and check utility.
 * This class will assist in users' password management.
 */
public class PasswordUtils {
    // Alter the strength of encoder to increase or decrease the time taken to hash the password.
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Hashes the provided plain password for further storage.
     *
     * @param plainPassword the plain password
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    /**
     * Checks if the provided plain password matches the hashed password.
     *
     * @param plainPassword the plain password
     * @param hashedPassword the hashed password
     * @return true if the plain password matches the hashed password, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
