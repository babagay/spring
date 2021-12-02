package com.example.spring.aspect;

import com.example.spring.aop.StudentException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(3)
public class ExceptionHandlingAspect {

    @Before("Pointcuts.allGettersPointcut()")
    public void onBeforeGetExceptionHandlingAdvice(){
        System.out.println("Advice: BeforeGetException. Catch exceptions while getting a book. Order: 1");
    }

    @Before("Pointcuts.getUniLibraryBookByStringArgPointcut()")
    public void onGetBookByStringSecurityAdvice(){
        System.out.println("Advice: Before get book by name. Exception handling. Order: 3");
    }

    @Before("Pointcuts.addUniLibraryBookPointcut()")
    public void onAddBookHandlingAdvice(){
        System.out.println("Advice: Before add book. Exception handling. Order: 3");
    }

    // Срабатывает после выброса исключения
    // В любом случае - отловлено исключение или нет -
    //      если исключение поймано, адвайс срабатывает быстрее кода в блоке catch
    // [!] Через параметр throwing можно получить доступ к исключению
    //      И можно изменить его в адвайсе
    //      В случае изменения объекта исключения адвайсом, оно дойдет в модифицированном виде до
    //      обработчика в коде (в данном случае, до обработчика в тесте universityExceptionHandlingTest())
    //      Напр, здесь меняем сообщение, и оно будет измененным в тесте
    // [!] Адвайс НЕ может проглотить (обработать) исключение. Т.е. оно будет проброшено дальше
    @AfterThrowing(pointcut = "Pointcuts.getStudentNamePointcut()", throwing = "exception")
    public void onGetStudentExceptionAdvice(Throwable exception){
        StudentException e = (StudentException) exception;
        String mess = e.getMess() + ". Name is NULL";
        e.setMess(mess);

        System.out.println("Advice: On get Student Exception. Message: " + mess);
    }
}
