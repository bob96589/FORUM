package com.bob.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

public interface GenericDao<T> {
    T save(T model);

    void delete(T entity);

    T find(Class<T> clazz, Serializable pk);

    List<T> findAll(Class<T> clazz);

    List<T> find(Class<T> clazz, List<Criterion> criterions);

}
