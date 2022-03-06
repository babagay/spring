package com.example.spring.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * [!] похоже, что поинткаты работают только для прямого вызова методов, а не, когда тест дергает метод,
 * а аспект приложен к другому методу, который должен вызываться внутри теструемого
 * [!] аспекты работают для public методов и для protected - когда ты их вызываешь в тесте
 * [!] поинткаты должны быть НЕСТАТИЧЕСКИЕ
 */

@Aspect
@Component
public class Pointcuts {

    @Pointcut("execution(public String com.example.spring.aop.Library.get*(..))")
    public void allGettersPointcut() {
    }

    @Pointcut("execution(* com.example.spring.aop.UniLibrary.*(..))")
    public void anyUniLibraryMethodPointcut() {
    }

    @Pointcut("execution(void com.example.spring.aop.UniLibrary.*(*))")
    public void anyUniLibraryMethodAnyArgsPointcut() {
    }

    @Pointcut("execution(protected void com.example.spring.aop.UniLibrary.getBook(String))")
    public void getUniLibraryBookByStringArgPointcut() {
    }

    @Pointcut("execution(* com.example.spring.aop.UniLibrary.get*(..))")
    public void allUniLibraryArgumentedGettersPointcut() {
    }

    // [!] можно указать, что мы хотим видеть в качестве аргумента книгу: addBook(com.example.spring.aop.Book)
    @Pointcut("execution(void com.example.spring.aop.UniLibrary.addBook(..))")
    public void addUniLibraryBookPointcut() {
    }

    @Pointcut("execution(java.util.List com.example.spring.aop.University.getStudents(..))")
    public void getStudentsPointcut() {
    }

    @Pointcut("execution(* com.example.spring.aop.University.getStudentName*())")
    public void getStudentNamePointcut() {
    }

    @Pointcut("execution(* com.example.spring.aop.University.getStudent())")
    public void getStudentPointcut() {
    }

    @Pointcut("execution(* com.example.spring.mvc.controller.EmployeeController.*(..))")
    public void getAnyMethodEmployeeController() {
    }

}
