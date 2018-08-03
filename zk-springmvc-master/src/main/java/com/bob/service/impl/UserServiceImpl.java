package com.bob.service.impl;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bob.dao.UserDao;
import com.bob.model.User;
import com.bob.security.service.UsernamePasswordValidator;
import com.bob.service.UserService;
import com.bob.utils.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDao userDao;

    @Autowired
    private UsernamePasswordValidator usernamePasswordValidator;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userDao.findUsers();
    }

    @Override
    public User findUserById(Integer userId) {
        return userDao.find(User.class, userId);
    }

    @Override
    public void addUser(User user) {
        user.setPassword(PasswordEncoder.hashPassword(user.getPassword()));
        User existingUser = userDao.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new WebApplicationException("user exist");
        }
        userDao.save(user);
    }

    @Override
    public User UpdateUser(String username, String authority) {
        User user = userDao.findByUsername(username);
        user.setAuthority(authority);
        userDao.save(user);
        return user;
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        userDao.delete(user);
    }

    @Override
    public User changePwd(String username, String password, String newPassword) {
        User user = usernamePasswordValidator.validateCredentials(username, password);
        user.setPassword(PasswordEncoder.hashPassword(newPassword));
        userDao.save(user);
        return user;
    }

}
