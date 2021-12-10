package com.example.spring.aop;

import com.example.spring.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class UniversityTest {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    private final static University university = context.getBean("university", University.class);

    public static void main(String[] args) throws Exception {
        // universityTest();
        // universityExceptionHandlingTest();
        universityAroundAdviceTest();

        context.close();
    }

    /**
     * Здесь мы тестим AfterReturning advice,
     * который на лету меняет результат работы тестируемого метода getStudents()
     */
    private static void universityTest() {
        List<Student> students = university.getStudents();
    }

    /**
     * Тест AfterThrowing адвайса
     */
    private static void universityExceptionHandlingTest() {
        try {
            String name = university.getStudentNameWithException();
        } catch (StudentException e){
            System.out.println("Test. getStudentNameWithException() throws exception: " + e.getMess());
        }
    }

    /**
     * Тест Around адвайса
     */
    private static void universityAroundAdviceTest() {
        Student st = university.getStudent();
        System.out.println("Test. universityAroundAdviceTest(). Student: " + st);
    }
}
