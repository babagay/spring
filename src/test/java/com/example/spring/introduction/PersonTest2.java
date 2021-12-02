package com.example.spring.introduction;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PersonTest2
{
    private final static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("./appContextWithAnnotations.xml");

    public static void main(String[] args) {

        Person person = context.getBean("thePerson", Person.class);

        person.callYourPet();

        System.out.println(person.getName());
        System.out.println(person.getAge() + 3);
        
        context.close();
    }
}
