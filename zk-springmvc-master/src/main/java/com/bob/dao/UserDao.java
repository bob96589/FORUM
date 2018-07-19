package com.bob.dao;

import java.util.List;

import com.bob.model.User;

public interface UserDao extends GenericDao<User> {

    User findByUsername(String username);

    List<User> findUsers();

}
