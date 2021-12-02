package com.example.spring.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public aspect SomeAspect {

    //@Pointcut("@annotation(AspectAnnotation)")
    @Pointcut("execution(* com.example.spring.aop.SchoolLibrary.break*())")
    public void breakBookPointcut(){}

    // [!] не работает
    @After("breakBookPointcut()")
    public void onAfterBreakingBook(){
        System.out.println("Aspect: on after break<ANY>() advice works");
    }


}
