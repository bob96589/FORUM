package com.bob.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bob.dao.TagDetailDao;

@Repository
public class TagDetailDaoImpl implements TagDetailDao {
	
    @Autowired
    SessionFactory session;

}
