package com.example.spring.introduction;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.example.spring.AppConstants.*;

/**
 * Full XML-less config
 * https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.spring.db", entityManagerFactoryRef = "entityManagerFactory")
@EntityScan(basePackages = "com.example.spring.db")
public class DataConfig {

    @Autowired
    private Environment env;

    // todo
    // Проблема в том, что первый раз MetadataImplementor содержит маппинг Employee,
    // а во второй проход он уже пустой
    // ВОзникает ошибка Unable to locate persister: Employee
    // Происходит 2 попытки создать SessionFactoryImpl
    @Bean(name = "currentSession")
    public static Session getCurrentSession() {
        // Hibernate 5.4 SessionFactory example without XML

        Properties properties = new Properties();
        properties.put("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.put("hibernate.connection.url",
                     "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=my_db;responseBuffering=adaptive" // SelectMethod=direct;
                      );
        properties.put("hibernate.connection.username", "bestuser");
        properties.put("hibernate.connection.password", "J,]tlbYbnmcz3+idfL7NdjhWjV");
        properties.put("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        properties.put("hibernate.hbm2ddl.auto", "none");


        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration().setProperties(properties);

        SessionFactory sessionFactory = cfg.buildSessionFactory();

        /*
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(settings).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        // metadataSources.addAnnotatedClass(Player.class);
        Metadata metadata = metadataSources.buildMetadata();

        // here we build the SessionFactory (Hibernate 5.4)
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        */

        Session session;
                try {
                    session = sessionFactory.getCurrentSession();
                } catch (HibernateException e){
                    session = sessionFactory.openSession();
                }
        return session;
    }

//    private static final SessionFactory sessionFactory = buildSessionFactory();
//
//    public static SessionFactory getSessionFactory() {
//        return sessionFactory;
//    }
//
//    private static SessionFactory buildSessionFactory() {
//        return new org.hibernate.cfg.Configuration().buildSessionFactory();
//    }

//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript("schema.sql")
//                //.addScript("data.sql")
//                .build();
//    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));

        return dataSource;
    }

    // todo попробовать заинжекать этот бин. Похоже, работает
    // To use JPA in a Spring project, we need to set up the EntityManager
    // Можно юзать LocalEntityManagerFactoryBean or the more flexible LocalContainerEntityManagerFactoryBean
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() { // можно без параметров либо с параматром DataSource dataSource
        LocalContainerEntityManagerFactoryBean emFactoryBean = new LocalContainerEntityManagerFactoryBean();
        emFactoryBean.setDataSource(dataSource());
        emFactoryBean.setPackagesToScan("com.example.spring.db");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emFactoryBean.setJpaVendorAdapter(vendorAdapter);
        emFactoryBean.setJpaProperties(hibProperties());
        emFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        return emFactoryBean;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }




    // transactionManager v1 - a
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(emf);
//        return transactionManager;
//    }

    // transactionManager v1 - b
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    // transactionManager v2
//    @Bean
//    public JpaTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory());
//        return transactionManager;
//    }

    // transactionManager v3
//    @Bean
//    public HibernateTransactionManager getTransactionManager() {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(getSessionFactory().getObject());
//        return transactionManager;
//    }

//    @Bean
//    public LocalSessionFactoryBean getSessionFactory() {
//        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//        factoryBean.setConfigLocation(context.getResource("classpath:hibernate.cfg.xml"));
//        factoryBean.setAnnotatedClasses(User.class);
//        return factoryBean;
//    }

//    @Bean(name="entityManagerFactory")
//    public LocalSessionFactoryBean sessionFactory() {
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        return sessionFactory;
//    }

    // ------------------------------




//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactoryBean.setDataSource(dataSource());
//        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
//
//        entityManagerFactoryBean.setJpaProperties(hibProperties());
//        entityManagerFactoryBean.setPackagesToScan("com.example.spring.db");
//        // entityManagerFactoryBean.setPersistenceUnitName("entityManagerFactory");
//
//        return entityManagerFactoryBean;
//    }

//    @Bean
//    @Qualifier(value = "entityManager")
//    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
//        return entityManagerFactory.createEntityManager();
//    }


    // список свойств: https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#
    private Properties hibProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
        properties.put("hibernate.show_sql", env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
        properties.put("hibernate.format_sql", env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
        properties.put("hibernate.use_sql_comments", true);
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DDL_AUTO));
        properties.put("hibernate.current_session_context_class", env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SESSION_CLASS));
        return properties;
    }

//    private Properties hibernateProperties() {
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
//        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        return hibernateProperties;
//    }






}
