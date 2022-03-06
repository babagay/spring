package com.example.spring.service;

public class AbstractService {
/*
    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private final static SessionFactory sessionFactory = context.getBean(SessionFactory.class); // OK
    // private final static SessionFactory sessionFactory = context.getBean(SessionFactory.class, "sessionFactory"); // так в сервисе может вылетать ошибка


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
 */
}
