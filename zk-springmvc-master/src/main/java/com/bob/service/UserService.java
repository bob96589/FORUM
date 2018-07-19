package com.bob.service;

import java.util.List;

import com.bob.model.User;

public interface UserService {

    List<User> getUsers();

    User findUserById(Integer userId);

    User findUserByUsername(String username);

    void addUser(User user);

    User UpdateUser(String username, String password, String authority);

    void deleteUserByUsername(String username);

}
