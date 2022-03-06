package com.example.spring.service;

import com.example.spring.db.Dao;
import com.example.spring.db.DetailsDao;
import com.example.spring.db.Employee;
import com.example.spring.db.EmployeeDao;
import com.example.spring.db.EmployeeDetails;
import com.example.spring.db.EmployeeDetailsRepository;
import com.example.spring.db.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    Session session;

    SessionFactory sessionFactory;

    PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(
            SessionFactory sessionFactory,
            Session session, // бин currentSession
            EmployeeRepository employeeRepository,
            EmployeeDetailsRepository detailsRepository,
            EmployeeDao employeeDao,
            DetailsDao detailsDao
            //PasswordEncoder passwordEncoder // todo проблема с инжектирванием из-за циклической зависимости            
                          ) {
        this.sessionFactory = sessionFactory;
        this.session = session;
        this.employeeRepository = employeeRepository;
        this.detailsRepository = detailsRepository;
        this.employeeDao = employeeDao; // Bean named 'employeeDao' is expected to be of type 'com.example.spring.db.EmployeeDao' but was actually of type 'com.sun.proxy.$Proxy96'
        this.detailsDao = detailsDao;
//        this.passwordEncoder = passwordEncoder;
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
     * [?] можно ли здесь юзать сессию
     */
    public Employee create(Employee employee) {

        // [!] если внутри getSession() указать имя бина "sessionFactory", через который берется сессия,
        //      получим PersistentObjectException: detached entity passed to persist: com.example.spring.db.Department
        //      Видимо, из-за того, что берутся 2 разные сессии
        // Session session1 = sessionFactory.getCurrentSession(); // HibernateException: Could not obtain transaction-synchronized Session for current thread
        // если юзать бин Session, получаем Could not obtain transaction-synchronized Session for current thread

        // Изначальный вариант, который уже НЕ работает
        //Session session1 = getSession();  
        //session1.beginTransaction();
        //employeeDao.create(employee);
        //session1.getTransaction().commit();

        // Session session = getSession(); // [!] обе сессии берут под капотм sessionFactory бин, поэтому, работают одинаково

        // todo
        // String pass = employee.getPassword();
        // pass = passwordEncoder.encode(pass);
        // employee.setPassword(pass);

        session.beginTransaction(); // [!] наконец, вот это можно вообще убрать
        employeeDao.create(employee);
        session.getTransaction().commit();

        return employee;
    }

    /**
     * Создаем через сессию
     */
    public Employee save(Employee employee) {
        //Session session = getSession();
        session.beginTransaction();
        session.save(employee);
        session.getTransaction().commit();
        return employee;
    }

    public EmployeeDetails save(EmployeeDetails details) {
        //Session session = getSession();
        session.beginTransaction();
        session.save(details);
        session.getTransaction().commit();
        return details;
    }

    public void dropById(Long id) {
        employeeRepository.deleteById(id);
    }

    public void dropDetails(Long id) {
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
        //Session session = getSession();
        session.beginTransaction();
        session.createNativeQuery("UPDATE employees set surname = :surname where id = :id", Employee.class)
                .setParameter("id", user.getId())
                .setParameter("surname", user.getSurname())
                .executeUpdate();

        session.getTransaction().commit();
        return user;
    }

    public EmployeeDetails detailsById(Long id) {
        //Session session = getSession();
        session.beginTransaction();

        EmployeeDetails details;
        details = detailsDao.get(id).orElseGet(null);

        session.getTransaction().commit();
        session.close();

        return details;
    }

    public boolean exist(EmployeeDetails details) {
        Example<EmployeeDetails> dTail = Example.of(details);
        return detailsRepository.exists(dTail);
    }

    // наверное, это не правильно - сохранять детали через employeeDao вместо employeeDetailsDao
    public void createDetails(EmployeeDetails details) {
        employeeDao._create(details);
    }

    public Employee findByUsername(String name) {
        return employeeRepository.retrieveByName(name);
    }
}
