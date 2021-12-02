package com.example.spring.aop;

import org.springframework.stereotype.Component;

@Component("schoolLib")
public class SchoolLibrary extends AbstractLibrary {
    @Override
    public void getBook() {
        System.out.println("JOB: schoolLib.getBook()");
    }

    public void breakBook(String book){
        System.out.println("JOB: schoolLib.breakBook() " + book);
    }
}
