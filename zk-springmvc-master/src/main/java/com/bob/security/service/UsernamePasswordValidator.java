package com.bob.security.service;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.bob.model.User;
import com.bob.security.exception.AuthenticationException;
import com.bob.service.UserService;

/**
 * Component for validating user credentials.
 *
 * @author cassiomolin
 */
// @ApplicationScoped
@Component
public class UsernamePasswordValidator {

    @Inject
    private UserService userService;

    @Inject
    private PasswordEncoder passwordEncoder;

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
            // User cannot be found with the given username/email
            throw new AuthenticationException("Bad credentials.");
        }

        // if (!user.isActive()) {
        // // User is not active
        // throw new AuthenticationException("The user is inactive.");
        // }

        // if (!password.equals(user.getPassword())) {
        if (!passwordEncoder.checkPassword(password, user.getPassword())) {
            // Invalid password
            throw new AuthenticationException("Bad credentials.");
        }

        return user;
    }
}