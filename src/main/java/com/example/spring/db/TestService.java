package com.example.spring.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    
    @Autowired
    public TestService(SessionFactory sessionFactory,
                       Session session, // бин currentSession
                       EmployeeRepository employeeRepository,
                       EmployeeDetailsRepository detailsRepository,
                       DetailsDao detailsDao) {
    }
}
