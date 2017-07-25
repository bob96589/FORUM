package com.bob.dao.impl;

import java.lang.reflect.ParameterizedType;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bob.dao.GenericDao;
import com.bob.utils.SqlStatement;

public class GenericDaoImpl<T> implements GenericDao<T> {
	
	
	protected Class<T> type;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    SqlStatement sqlStatement;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public GenericDaoImpl() {
        try {
            type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (ClassCastException e) {
            Class<T> clazz = (Class<T>) getClass().getGenericSuperclass();
            type = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }
    
    

}
