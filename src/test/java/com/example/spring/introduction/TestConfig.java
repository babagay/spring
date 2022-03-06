package com.example.spring.introduction;

import com.example.spring.config.WebConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestConfig {

    // использование конфига на основе джава кода
    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

    public static void main(String[] args) {

        Person person = context.getBean(Person.class);

        person.callYourPet();
        System.out.println(person.getName());

        context.close();
    }
}
