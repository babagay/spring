package com.example.spring.db;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;


    public T create(T entity)
    {
        entityManager.persist(entity);
        return entity;
    }


}
