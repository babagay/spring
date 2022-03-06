package com.example.spring.db;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * инкапсулирует персистенс-слой
 * предоставляет CRUD-операции
 */
@Component
// @Transactional - может вызвать проблемы BeanNotOfRequiredTypeException: Bean named 'employeeDao' is expected to be of type 'com.example.spring.db.EmployeeDao' but was actually of type 'com.sun.proxy.$Proxy96'
// https://github.com/playframework/play-spring-loader/issues/13
public class EmployeeDao extends AbstractDao<Employee> implements Dao<Employee> {

    public Employee findById(long id) {
        return entityManager.find(Employee.class, id);
    }

    public Employee findByName(String name) {
        return entityManager.find(Employee.class, name);
    }

    // пример HQL
    @Override
    public Optional<Employee> get(long id) {
        return Optional.ofNullable(
                        entityManager.createQuery("from Employee e join fetch e.details where e.id = :id  ", Employee.class)
                                .setParameter("id", id)
                                .getResultList()
                                  )
                .map(users -> {
                    if (users != null && users.size() > 0) {
                        return users.get(0);
                    }
                    return null;
                });
    }

    // не работает
    public List getSeq() {
        Query d = entityManager.createNativeQuery("SELECT * FROM INFORMATION_SCHEMA.SEQUENCES");
        List u = d.getResultList();
        return u;
    }

    @Override
    public Collection<Employee> getAll() {
        // HQL
        return entityManager.createQuery("from Employee e order by e.id desc", Employee.class).getResultList();

        // SQL
        // return (List<Employee>) entityManager.createNativeQuery("SELECT * FROM employees", Employee.class).getResultList();

        // [!] Можно работать так:
        // entityManager.createNativeQuery("delete from Foo where id = :id")
        //   .setParameter("id", 1)
        //   .executeUpdate();
    }

    @Override
    public long save(Employee employee) {
        // todo
        return 0;
    }

    @Override
    public void update(Employee employee) {
        // todo
    }

    @Override
    public void delete(Employee employee) {
        Employee em = entityManager.merge(employee);
        entityManager.remove(em);
        entityManager.flush();
        entityManager.clear();
    }

    // [!] Выдавало ошибку
    // No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call
    // Вылечила аннотация @Transactional
    public Employee _create(Employee user) {
        // entityManager.persist(user); // OK
        return create(user); // OK - персист через entityManager
    }

    public void _create(EmployeeDetails details) {
        entityManager.persist(details);
    }


}
