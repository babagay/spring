package com.example.spring.db;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {

    @Override
    <S extends EmployeeDetails> boolean exists(Example<S> example);

    @Override
    @Modifying
    @Query("DELETE FROM EmployeeDetails d WHERE d.id = :id")
    void deleteById(@Param("id") Long aLong);
}