package com.example.spring.aop;

import org.springframework.stereotype.Component;

@Component("uniLib")
public class UniLibrary extends AbstractLibrary {
    @Override
    public void getBook() {
        System.out.println("JOB: uniLib.getBook()");
    }

    public void getBook(Book book) {
        System.out.println("JOB: uniLib.getBook() " + book.getName());
    }

    protected void getBook(String book) {
        System.out.println("JOB: UniLibrary.getBook() by name: '" + book + "'");
    }

    public void getBookByName(String book){
        getBook(book);
    }

    public void getMagazine() {
        System.out.println("JOB: uniLib.getMagazine()");
    }

    public void printBookById(int id){
        System.out.println("JOB: uniLib.printBookById() " + id);
    }

    public void addBook(Book book){
        System.out.println("JOB: uniLib.printBookById() " + book.getName() + " " +
                book.getAutor() + " " +
                book.getPublishYear());
    }

    public void addBook(String name, String author, int year){
        System.out.println("JOB: uniLib.printBookById() " + name + " " +
                                   author + " " +
                                   year);
    }

}
