package com.example.spring.aop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Book {

    @Value("The crime and the punishment")
    private String name;

    @Value("Dostoevskiy")
    private String autor;

    @Value("1866")
    private int publishYear;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutor() {
        return autor;
    }

    public int getPublishYear() {
        return publishYear;
    }
}
