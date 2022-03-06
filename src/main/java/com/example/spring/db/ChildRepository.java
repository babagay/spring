package com.example.spring.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@EnableTransactionManagement
public interface ChildRepository extends JpaRepository<Child, Integer> {

    @Override
    @Query("select c from Child c where c.id = ?1")
    Optional<Child> findById(Integer integer);
}
