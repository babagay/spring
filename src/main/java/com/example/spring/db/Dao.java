package com.example.spring.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Optional;

public interface Dao<T> {

//    @PersistenceContext
//    EntityManager entityManager = null;

    Optional<T> get(long id);

    Collection<T> getAll();

    long save(T t);

    void update(T t);

    void delete(T t);

    // [!] не работает т.к. здесь entityManager = null
//    default T create(T entity) {
//
//        if (entityManager != null) {
//            entityManager.persist(entity);
//        }
//        return entity;
//    }
}
