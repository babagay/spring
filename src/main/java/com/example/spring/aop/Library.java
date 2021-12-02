package com.example.spring.aop;

import org.springframework.stereotype.Component;

@Component("libraryBean")
public class Library {


    public void getBook() {
        System.out.println("JOB: Library. Get book");
    }

    public String returnBook() {
        System.out.println("JOB: Library. return Book String");
        return "";
    }

    public void returnBookVoid() {
        System.out.println("JOB: Library. return Book void");
    }

    public void returnMagazine(){
        System.out.println("JOB: Library. Magazine returned");
    }

    public void addBook(){
        System.out.println("JOB: Library. Add book");
    }

    public void addMagazine(){
        System.out.println("JOB: Library. Add Magazine");
    }

}
