package com.example.spring.db;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// @Transactional
// [!] присутсвие аннотации вызывало ошибку при подключении даошек, как DetailsDao,
// при старте через консольное (а не web) приложение
// No qualifying bean of type 'org.springframework.security.crypto.password.PasswordEncoder' available: 
// expected at least 1 bean which qualifies as autowire candidate.
public class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;


    public T create(T entity)
    {
        entityManager.persist(entity);
        return entity;
    }


}
