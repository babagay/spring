package com.example.spring.aop;

import com.example.spring.introduction.ApplicationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// тестирование аспектов на примере Library
class LibraryTest {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

    private final static Library lib = context.getBean("libraryBean", Library.class);
    private final static UniLibrary uniLib = context.getBean("uniLib", UniLibrary.class);
    private final static AbstractLibrary schoolLib = context.getBean("schoolLib", SchoolLibrary.class);
    private final static Book book = context.getBean( Book.class);

    public static void main(String[] args) {

        uniLibraryAddBookTest();

        context.close();
    }

    private static void libraryTest() {
        // тест аспектов на примере класса Library

        lib.getBook();
        lib.returnBook();
        lib.returnBookVoid();
    }

    private static void uniLibraryTest() {
        uniLib.printBookById(138);
    }

    private static void uniLibraryTest2() {
         uniLib.getBook();
         uniLib.getMagazine();
    }

    private static void getBookTest() {
        // Для UniLibrary.getBook() выполнятся несколько аспектов:
        // Aspect. Getting something - наиболее общий
        // Aspect. Getting the book
        // Aspect. Getting the UniLibrary book - наиболее спицифичный
        // JOB: uniLib.getBook() - отработка самого метода

        uniLib.getBook();
        uniLib.getMagazine();
    }

    private static void schoolLibGetBookTest() {
        schoolLib.getBook();
    }

    private static void breakBookTest() {
        // [!] не смотря на то, что тип schoolLibrary указан как AbstractLibrary, выполняется код из класса SchoolLibrary
        AbstractLibrary schoolLibrary = context.getBean("schoolLib", SchoolLibrary.class);
        schoolLibrary.breakBook("Юность");
    }

    private static void bookTest(){
        uniLib.getBook(book);
    }

    private static void libraryGetterTest(){
        lib.getBook();
    }

    private static void libraryReturnerTest(){
        lib.returnMagazine();
    }

    private static void uniLibraryGetBookByNameTest(){
        // aspect ordering test
        // Security - first
        // LoggingAspect - second
        // Exception handling - third
        uniLib.getBook("test");
    }

    // тестирование доступа к аргуентам метода в аспекте
    private static void uniLibraryAddBookTest(){
        book.setName("Karamazov brothers");
        uniLib.addBook(book); // test 1
        // uniLib.addBook("I-fuck", "Pelevin", 2010); // test 2
    }
}