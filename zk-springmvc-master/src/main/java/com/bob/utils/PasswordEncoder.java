package com.bob.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * bcrypt password encoder.
 *
 * @author cassiomolin
 */
public class PasswordEncoder {

    /**
     * Hashes a password using BCrypt.
     *
     * @param plainTextPassword
     * @return
     */
    public static String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    /**
     * Checks a password against a stored hash using BCrypt.
     *
     * @param plainTextPassword
     * @param hashedPassword
     * @return
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {

        if (null == hashedPassword || !hashedPassword.startsWith("$2a$")) {
            throw new RuntimeException("Hashed password is invalid");
        }

        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public static void main(String[] args) {
        System.out.println(hashPassword("password"));
    }
}