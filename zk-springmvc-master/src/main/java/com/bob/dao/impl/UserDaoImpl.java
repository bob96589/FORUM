package com.bob.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bob.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {
	
    @Autowired
    SessionFactory session;

}
