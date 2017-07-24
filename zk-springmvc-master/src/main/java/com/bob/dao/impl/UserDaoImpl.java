package com.bob.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bob.dao.UserDao;
import com.bob.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	SessionFactory session;

	@Override
	public User findByAccount(String username) {
		Query query = session.getCurrentSession().createQuery("FROM User U WHERE U.account = :account");
		query.setParameter("account", username);
		User user = (User) query.uniqueResult();
		return user;
	}

}
