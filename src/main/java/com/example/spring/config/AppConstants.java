package com.example.spring.config;

public interface AppConstants {

    String PROPERTY_NAME_DATABASE_DRIVER = "spring.datasource.driver-class-name";
    String PROPERTY_NAME_DB_DRIVER = "spring.datasource.driverClassName";
    String PROPERTY_NAME_DATABASE_URL = "spring.datasource.url";
    String PROPERTY_NAME_DATABASE_USERNAME = "spring.datasource.username";
    String PROPERTY_NAME_DATABASE_PASSWORD = "spring.datasource.password";
    String PROPERTY_NAME_HIBERNATE_DIALECT = "spring.jpa.properties.hibernate.dialect";
    String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "spring.jpa.show-sql";
    String PROPERTY_NAME_HIBERNATE_DDL_AUTO = "spring.jpa.hibernate.ddl-auto";
    String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "spring.jpa.properties.hibernate.format_sql";
    String PROPERTY_NAME_HIBERNATE_USE_SQL_COMMENTS = "hibernate.use_sql_comments"; // кастомное свойство
    String PROPERTY_NAME_HIBERNATE_SESSION_CLASS = "spring.jpa.properties.hibernate.current_session_context_class";
    String APPLICATION_NAME = "app.name";
}
