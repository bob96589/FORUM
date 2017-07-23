package com.bob.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bob.dao.TagDao;
import com.bob.model.Tag;

@Repository
public class TagDaoImpl implements TagDao {
	
    @Autowired
    SessionFactory sessionFactory;
    
    public void saveOrUpdate(Tag tag) {
    	sessionFactory.getCurrentSession().persist(tag);
    }

}
