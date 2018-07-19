package com.bob.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bob.dao.UserDao;
import com.bob.model.User;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    @Override
    public User findByUsername(String username) {
        Query query = getSessionFactory().getCurrentSession().createQuery("FROM User U WHERE U.username = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();
        return user;
    }

    @Override
    public List<User> findUsers() {
        Session session = getSessionFactory().getCurrentSession();
        Query query = session.createQuery("FROM User");
        List<User> list = query.list();
        return list;
    }

}
