package com.bob.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;

import com.bob.dao.GenericDao;
import com.bob.utils.CustomNamedParameterJdbcTemplate;

public class GenericDaoImpl<T> implements GenericDao<T> {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private CustomNamedParameterJdbcTemplate CustomNamedParameterJdbcTemplate;

    @Override
    public T save(T model) {
        sessionFactory.getCurrentSession().saveOrUpdate(model);
        return model;
    }

    @Override
    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);

    }

    @Override
    public T find(Class<T> clazz, Serializable pk) {
        return (T) sessionFactory.getCurrentSession().get(clazz, pk);
    }

    @Override
    public List<T> findAll(Class<T> clazz) {
        return sessionFactory.getCurrentSession().createCriteria(clazz).list();
    }

    @Override
    public List<T> find(Class<T> clazz, List<Criterion> criterions) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria.list();
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected CustomNamedParameterJdbcTemplate getCustomNamedParameterJdbcTemplate() {
        return CustomNamedParameterJdbcTemplate;
    }

}
