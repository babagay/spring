package com.example.spring.aop;

public abstract class AbstractLibrary {

    public abstract void getBook();

    public void breakBook(String name){
        System.out.println("JOB: AbstractLibrary.break the Book. " + name);
    }
}
