package com.bob.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bob.security.ForumUserDetails;
import com.bob.service.UserService;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        com.bob.model.User dbUser = userService.findUserByUsername(username);
        if (dbUser == null) {
            throw new UsernameNotFoundException("account " + username + " could not be found");
        }
        return new ForumUserDetails(dbUser.getId(), dbUser.getUsername(), dbUser.getPassword().toLowerCase(), true, true, true, true, new ArrayList<GrantedAuthority>());
    }
}
