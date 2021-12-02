package com.example.spring.db;

import com.example.spring.aop.Student;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtils {

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    static {

        try {
            Logger.getLogger("org.hibernate").setLevel(Level.ALL);

            Configuration configuration = new Configuration();

            Properties settings = new Properties();
            settings.put(Environment.DRIVER, "${spring.datasource.driver-class-name}");
            settings.put(Environment.URL, "jdbc:mysql://localhost:3306/java_dmeo?useSSL=false");
            settings.put(Environment.USER, "root");
            settings.put(Environment.PASS, "root");
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            settings.put(Environment.HBM2DDL_AUTO, "create-drop"); // hibernate.hbm2ddl.auto = update

            configuration.setProperties(settings);

            configuration.addAnnotatedClass(Employee.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

//            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException exception) {
            System.out.println("Problem creating session factory");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
