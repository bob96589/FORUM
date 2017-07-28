package com.bob.dao.impl;

import java.util.List;

import org.hibernate.Query;
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
		sessionFactory.getCurrentSession().saveOrUpdate(tag);
	}

	@Override
	public List<Tag> getAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Tag");
		List<Tag> list = query.list();
		return list;
	}

}
