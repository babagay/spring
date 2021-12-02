package com.example.spring.db;

import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.Optional;

@Component
public class DetailsDao extends AbstractDao<EmployeeDetails> implements Dao<EmployeeDetails> {


    @Override
    public Optional<EmployeeDetails> get(long id) {
        //return Optional.ofNullable(entityManager.find(EmployeeDetails.class, id, LockModeType.READ));
        return Optional.ofNullable(entityManager.find(EmployeeDetails.class, id));
    }

    @Override
    public Collection<EmployeeDetails> getAll() {
        return null;
    }

    @Override
    public long save(EmployeeDetails details) {
        return 0;
    }

    @Override
    public void update(EmployeeDetails details) {

    }

    @Override
    public void delete(EmployeeDetails details) {

    }
}