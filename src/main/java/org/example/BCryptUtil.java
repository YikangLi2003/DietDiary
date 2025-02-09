package org.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt password hashing and check utility.
 * This class will assist in users' password management.
 */
public class BCryptUtil {
    // The strength of the BCrypt algorithm is defined by the number of iterations.
    // The higher the number, the more secure the password hash will be.
    private static final int bcryptStrength = 12;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(bcryptStrength);

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
