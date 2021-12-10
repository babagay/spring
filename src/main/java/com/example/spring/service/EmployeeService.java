package com.example.spring.service;

import com.example.spring.db.DepartmentRepository;
import com.example.spring.db.DetailsDao;
import com.example.spring.db.Employee;
import com.example.spring.db.EmployeeDao;
import com.example.spring.db.EmployeeDetails;
import com.example.spring.db.EmployeeDetailsRepository;
import com.example.spring.db.EmployeeRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
//@Scope(value = "session")
public class EmployeeService extends AbstractService {

    // @Autowired
    EmployeeRepository employeeRepository;

    // @Autowired
    EmployeeDetailsRepository detailsRepository;

    // @Autowired
    EmployeeDao employeeDao;

    // @Autowired
    DetailsDao detailsDao;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeDetailsRepository detailsRepository,
                           DepartmentRepository departmentRepository,
                           EmployeeDao employeeDao,
                           DetailsDao detailsDao) {
        this.employeeRepository = employeeRepository;
        this.detailsRepository = detailsRepository;
        this.employeeDao = employeeDao;
        this.detailsDao = detailsDao;
    }

    public Employee byId(long id) {
        return employeeDao.findById(id);
    }

    public Optional<Employee> getById(long id) {
        return employeeDao.get(id);
    }

    public Collection<Employee> all() {
        return employeeDao.getAll();
    }

    public Employee bySurname(String surname) {
        return employeeRepository.findBySurname(surname);
    }

    public List getSeq() {
        return employeeDao.getSeq();
    }

    /**
     * Создаем через метод DAO, у которого внутри entityManager
     */
    public Employee create(Employee employee) {
        Session session = getSession();
        session.beginTransaction();
        employeeDao.create(employee);
        session.getTransaction().commit();
        return employee;
    }

    /**
     * Создаем через сессию
     */
    public Employee save(Employee employee) {
        Session session = getSession();
        session.beginTransaction();
        session.save(employee);
        session.getTransaction().commit();
        return employee;
    }

    public EmployeeDetails save(EmployeeDetails details) {
        Session session = getSession();
        session.beginTransaction();
        session.save(details);
        session.getTransaction().commit();
        return details;
    }

    public void dropById(Long id) {
        employeeRepository.deleteById(id);
    }

    public void dropDetails(Long id){
        detailsRepository.deleteById(id);
    }

    public void dropEntity(Employee user) {
        if (user != null) {
            employeeDao.delete(user);
        }
    }

    // todo
    // Думаю, сюда надо передавать DTO вместо Employee
    // доставать Employee, сэтать его поля
    // И потом комитать сессию
    public Employee update(Employee user) {
        Session session = getSession();
        session.beginTransaction();
        session.createNativeQuery("UPDATE employees set surname = :surname where id = :id", Employee.class)
                .setParameter("id", user.getId())
                .setParameter("surname", user.getSurname())
                .executeUpdate();

        session.getTransaction().commit();
        return user;
    }

    public EmployeeDetails detailsById(Long id) {
        Session session = getSession();
        session.beginTransaction();

        EmployeeDetails details;
        details = detailsDao.get(id).orElseGet(null);

        session.getTransaction().commit();
        session.close();

        return details;
    }

    public boolean exist(EmployeeDetails details){
        Example<EmployeeDetails> dTail = Example.of(details);
        return detailsRepository.exists(dTail);
    }

    // наверное, это не правильно - сохранять детали через employeeDao вместо employeeDetailsDao
    public void createDetails(EmployeeDetails details) {
        employeeDao._create(details);
    }

}
