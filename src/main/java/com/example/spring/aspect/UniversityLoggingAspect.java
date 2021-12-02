package com.example.spring.aspect;

import com.example.spring.aop.Student;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
public class UniversityLoggingAspect {

    @Before("execution(* getStudents())")
    public void beforeGetStudentsLoggingAdvice(){
        System.out.println("Advice: Get students.");
    }

    // [!] AfterReturning адвайс выполняется только после нормального окончания работы таргет-метода,
    //      но ДО присвоения результата работы таргет-метода переменной
    // [!] AfterReturning адвайс меняет результат работы таргет-метода на лету
    //      - на примере изменения данных последнего студента в списке
    // [!] Т.к. AfterReturning адвайс меняет выходные данные,
    //      надо где-то обозначить этот факт. Например, в комментариях к таргет-методу
    @AfterReturning(
            // value = "execution(* getStudents())", // либо указываем выражение, по которому ищется метод,
            pointcut = "Pointcuts.getStudentsPointcut()", // либо имя поинтката - работает и так, и так
            returning = "result"
            // argNames = "result" // зачем это, не понял. Работает без него
    )
    public void afterReturningGetStudentsLoggingAdvice(JoinPoint joinPoint, List<Student> result){

        Student testedStudent = null; // тестируемый студент, последний в списке студентов
        double gradeBefore = 0;

        // [!] можно так. Если аргумент типа Object result
//        if(result instanceof List){
//            List<Student> students = (List<Student>) result;
//            testedStudent = students.get(students.size() - 1);
//        }
        // [!] но можно аргументу адвайса сразу назначить нужный тип : List<Student> result
        testedStudent = result.get(result.size() - 1);
        gradeBefore = testedStudent.getAvgGrade();
        testedStudent.setAvgGrade(5.6);
        testedStudent.setName(testedStudent.getName() + " Mitaca");

        System.out.println("Advice: After returning students. New student: " + testedStudent.getName()
                          + ". grade before aspect worked: " + gradeBefore + ". "
                           + " grade after aspect worked: " + testedStudent.getAvgGrade()
                );
        System.out.println(result);
    }
}
