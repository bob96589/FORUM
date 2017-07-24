package com.bob.dao;

import com.bob.model.User;

public interface UserDao {

	User findByAccount(String username);

}
