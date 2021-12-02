package com.example.spring.db;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {

    @Override
    <S extends EmployeeDetails> boolean exists(Example<S> example);
}