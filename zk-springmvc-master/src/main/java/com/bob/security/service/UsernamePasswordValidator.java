package com.bob.security.service;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.bob.model.User;
import com.bob.security.exception.AuthenticationException;
import com.bob.service.UserService;
import com.bob.utils.PasswordEncoder;

/**
 * Component for validating user credentials.
 *
 * @author cassiomolin
 */
@Component
public class UsernamePasswordValidator {

    @Inject
    private UserService userService;

    /**
     * Validate username and password.
     *
     * @param username
     * @param password
     * @return
     */
    public User validateCredentials(String username, String password) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            // User cannot be found with the given username
            throw new AuthenticationException("Bad credentials.");
        }
        if (!PasswordEncoder.checkPassword(password, user.getPassword())) {
            // Invalid password
            throw new AuthenticationException("Bad credentials.");
        }
        return user;
    }
}