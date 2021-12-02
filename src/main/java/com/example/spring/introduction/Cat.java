package com.example.spring.introduction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component("theCat")
@Scope("singleton")
public class Cat implements Pet {

    @Value("${cat.name}")
    private String name;

    public Cat() {
        System.out.println("Cat created: " + this);
    }

    @Override
    public void say() {
        System.out.println("Mew!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PostConstruct
    void init(){
        System.out.println("Cat Init");
    }

    void initXML(){
        System.out.println("Cat Init from XML");
    }

    @PreDestroy
    void destroy(){
        System.out.println("Cat destroy " + this);
    }

    void destroyXML(){
        System.out.println("Cat destroy from XML " + this);
    }


}
