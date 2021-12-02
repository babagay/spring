package com.example.spring.introduction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private static ClassPathXmlApplicationContext context;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("./applicationContext.xml");

        // [!] если в xml-нике указан spring.introduction.Cat,
        // здесь мы можем вызвать через getBean(Pet.class) или getBean(Cat.class)
        // Менять зверя (т.е. имплементацию) надо в context-файле
        // Pet pet = context.getBean(Pet.class);

        Person person = context.getBean("thePerson", Person.class); // вместо new Person(pet);
        // имя "thePerson" м.б. опущено

        // Person person = new Person(pet);

        person.callYourPet();

        System.out.println(person.getAge());
        System.out.println(person.getName());

        context.close();
    }
}