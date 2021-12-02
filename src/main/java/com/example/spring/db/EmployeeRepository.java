package com.example.spring.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// [?] чем отличается концепция repository от DAO
// В проекте social все наоборт:
// - репозиторий класс а не интерфейс
// - DAO интерфейс наследующий JpaRepository, без аннотаций
//      с методами типа findByTitle()
@Repository
@Transactional
@EnableTransactionManagement
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("select e from Employee e where e.id = ?1")
    Employee retrieveById(Long id);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) = LOWER(:name)")
    Employee retrieveByName(@Param("name") String name);


    Employee findBySurname(String surname); // метод сгенерится автоматом

    @Modifying
    @Query("DELETE FROM Employee e WHERE e.id = :id")
    void deleteById(@Param("id") Long id);
}
