package com.example.spring.introduction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;


class CatTest {

    static ClassPathXmlApplicationContext context;

    @BeforeTestClass
    public void beforeAll() {
        context = new ClassPathXmlApplicationContext("./applicationContext.xml");
    }


//    простой тест (без спринга)
//    public static void main(String[] args) {
//        CatTest test = new CatTest();
//        test.testCatSay();
//    }

    public static void main(String[] args) {
        CatTest test = new CatTest();
        context = new ClassPathXmlApplicationContext("./applicationContext.xml");

        test.petSay();

        context.close();
    }

    void petSay() {
        // Вместо того, чтобы создавать объект вручную, мы получаем его через Spring посредством IoC
        // Pet cat = new Cat();

        Pet pet = context.getBean("myPet", Pet.class);

        // [!] благодаря тому, что здесь мы тестируем на уровне интерфейсов,
        // мы можем поменять реализацию с Cat на Dog в applicationContext -
        // <bean id="myPet" class="com.example.spring.introduction.Dog" autowire="byType"/>
        // - и здесь, в petSay(), получим бин Dog вместо Cat
        // [!] Важно то, что applicationContext это текстовый файл,
        // что позволяет подменять реализацию без перекомпиляции приложения

        pet.say();
    }
}