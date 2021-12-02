package com.example.spring.aspect;

import com.example.spring.aop.Book;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1) // задать порядок выполнения аспектов. 1 - самый высокий приоритет
public class SecurityAspect {

    @Before("Pointcuts.getUniLibraryBookByStringArgPointcut()")
    public void onGetBookByStringSecurityAdvice(){
        System.out.println("Advice: Before get book by name. Access checking. Order: 1");
    }

    @Before("Pointcuts.addUniLibraryBookPointcut()")
    public void onAddBookSecurityAdvice(JoinPoint joinPoint){
       System.out.println("Advice: Before add book. Access checking. Order: 1");
    }
}
