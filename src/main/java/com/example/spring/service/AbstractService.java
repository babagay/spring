package com.example.spring.service;

import com.example.spring.introduction.ApplicationConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AbstractService {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    private final static SessionFactory sessionFactory = context.getBean(SessionFactory.class);


    protected static Session getSession() {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
            System.out.println("AbstractService. session = getCurrentSession()");
        }
        catch (HibernateException e) {
            session = sessionFactory.openSession();
            System.out.println("AbstractService. session = openSession()");
        }
        return session;
    }
}
