package com.example.spring.aspect;

import com.example.spring.aop.Book;
import com.example.spring.aop.Student;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * [!] по умолчанию аннотация не доступна - надо вручную добавить пакет AspectJ Weaver
 * ставил очень сложно
 * AspectWeaver видимо, не сработал
 * В итоге, сделал по этой доке https://www.jetbrains.com/help/idea/2021.1/aspectj.html
 * <p>
 * [!] более спицифичный адвайс сработает после более общих адвайсов
 */
@Component
@Aspect // тип LoggingAspect работает как аспект
@Order(2)
public class LoggingAspect {

    // Before - разновидность адвайса (т.е. хука) - кода, содержащего некую сквозную логику
    // аргументом адвайса (поинт-катом) выступает метод, на который вешается хук
    //  для написания используется AspectJ Pointcut expression Language
    //  Шаблон: execution(modifier return_type declaring_type method_name(params) throws), где каждая позиция работает как паттерн
    //  Т.о. этот аспект применится ко всем классам, где есть метод с подобной сигнатурой, приватностью и бросаемым исключением
    // Название метода (хука) м.б. любым

    @Before("execution(public void getBook())")
    public void beforeGettingBookAdvice() {
        System.out.println("Aspect. Getting the book");
    }

    @Before("execution(public void com.example.spring.aop.UniLibrary.getBook())")
    public void beforeGettingBookUniLibraryAdvice() {
        System.out.println("Aspect. Getting the UniLibrary book");
    }

    @Before("execution(public void com.example.spring.aop.UniLibrary.get*())")
    public void beforeGetterAdvice() {
        System.out.println("Aspect. Getting something");
    }

    // Сработает для:
    // - void returnBookVoid()
    // - string returnBook()
    @Before("execution(public * return*())")
    // @Before("execution(* return*())") - то же, для любого access modifier
    // @Before("execution(* *())") - шаблон означает "любой access modifier с любым return типом любого метода без параметров"
    public void beforeReturnBookAdvice() {
        System.out.println("Aspect. Return any");
    }

    // полиморфизм в Pointcut expression Language работает,
    // поэтому, если указан тип AbstractLibrary, хук сработает и на SchoolLibrary.breakBook()
    // Хук применяется к методам, начинающимся на 'break' с параметром String
    @After("execution(* com.example.spring.aop.AbstractLibrary.break*(String))")
    public void onAfterBreakingTheBookAdvice() {
        System.out.println("Advice: breakBook() After hook works");
    }

    // применимо для любого void метода класса UniLibrary с единственным параметром любого типа
    @After("Pointcuts.anyUniLibraryMethodAnyArgsPointcut()")
    public void uniLibraryAnyMethodAdvice() {
        System.out.println("Advice: UniLibrary.any() After hook works");
    }

    // применимо для любого [void|public etc] метода класса UniLibrary с любым количеством параметров любого типа. Подходит для перегрузок методов
    // [!] After адвайс срабатывает всегда:
    //  - метод отработал штатно
    //  - метод бросил исключение
    // По сути, это AfterFinally адвайс
    // [!] After адвайс НЕ позволяет получить доступ к результату работы таргет-метода
    //      Для этого нужен AfterReturning
    @After("Pointcuts.anyUniLibraryMethodPointcut()")
    public void uniLibraryAnyMethodAnyArgAdvice() {
        System.out.println("Advice: UniLibrary.any(..) After hook works");
    }

    // применимо для сигнатуры getBook(Book book)
    // или getBook(Book book, ...) у которого есть еще любые другие параметры
    @Before("execution(public * getBook(com.example.spring.aop.Book, ..))")
    public void beforeGetBookAdvice() {
        System.out.println("Aspect. Before getBook(Book)");
    }

    // определить поинткат, который можно юзать в нескольких адвайсах
    @Pointcut("execution(void get*())")
    private void anyGetterPointcut() {
    }

    @Pointcut("execution(public * com.example.spring.aop.Library.get*())")
    private void anyGetterLibraryPointcut() {
    }

    @Pointcut("execution(public * com.example.spring.aop.Library.return*())")
    private void anyReturnerLibraryPointcut() {
    }

    // объединение поинткатов
    @Pointcut("anyGetterLibraryPointcut() || anyReturnerLibraryPointcut()")
    private void anyGetOrReturnLibraryPointcut() {
    }

    @Before("anyGetterPointcut()")
    public void anyTypeGetMethodNoArgsAdvice() {
        System.out.println("Advice: get() Before hook works");
    }

    @Before("anyGetterPointcut()")
    public void beforeGetSecurityAdvice() {
        System.out.println("Advice: Security check before getting...");
    }

    @Before("anyGetterLibraryPointcut()")
    public void onGetAnyAdvice() {
        System.out.println("Advice: Before any Library getter");
    }

    @Before("anyReturnerLibraryPointcut()")
    public void onReturnAnyAdvice() {
        System.out.println("Advice: Before any Library returner");
    }

    @Before("anyGetOrReturnLibraryPointcut()")
    public void onReturnOrGetLibraryAdvice() {
        System.out.println("Advice: Before get|return Library");
    }

    @Before("Pointcuts.getUniLibraryBookByStringArgPointcut()")
    public void onGetBookByStringLoggingAdvice() {
        System.out.println("Advice: Before get book by name. Perform Logging. Order: 2");
    }

    @Before("Pointcuts.addUniLibraryBookPointcut()")
    public void onAddBookPerformLoggingAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String name = "";
        String author = "";
        int year = 0;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // получение инфы на основании парсинга сигнатур
        if (signature.getName().equals("addBook")) {
            if (args.length > 0 && args[0] instanceof Book) {
                Book book = (Book) args[0];
                name = book.getName();
                author = book.getAutor();
                year = book.getPublishYear();
            } else if (args.length > 0 && args[0] instanceof String) {
                name = (String) args[0];
                author = (String) args[1];
                year = (Integer) args[2];
            }
        }

        System.out.println("Advice: Before add book. Perform Logging. Adding book: '"
                                   + name
                                   + "' Year: " +
                                   year + " "
                                   + "author: " + author + ". "
                                   + "Order: 2");
        System.out.println(signature.getMethod());
        System.out.println(signature.getReturnType());
        System.out.println(signature.getName());
    }

    // Around адвайс вызывается ДО и ПОСЛЕ таргет-метода
    // [!] для пущего контроля таргет-метод нужно вызвать здесь же вручную
    //  По сути, является враппером
    // Around адвайс позволяет ловить исключения
    @Around(value = "Pointcuts.getStudentPointcut()")
    public Student onGetStudentAdvice(@NotNull ProceedingJoinPoint joinPoint) {
        String kind = joinPoint.getKind();
        SourceLocation location = joinPoint.getSourceLocation();
        Object target = joinPoint.getTarget();

        Student result;
        Student studentFake = new Student("Fake student", 5, 9.0);

        long time = 0;

        System.out.println("Advice: Around getting a Student.");

        try {
            long t1 = System.currentTimeMillis();
            result = (Student) joinPoint.proceed();
            result.setAvgGrade(result.getAvgGrade() + 1.);
            long t2 = System.currentTimeMillis();
            time = t2 - t1;
            // throw new Exception();
        }
        catch (Throwable e) {
            // [!] в этом адвайсе можно ловить исключения
            // Естественно, это не лучшее решение
            // Более адекватно - залогировать и пробросить исключение
            // throw new Exception("");
            e.printStackTrace();
            result = studentFake;
        }

        System.out.println("Advice: Around. Student got. " + kind + ". " + location + ". " + target + ". " + time);

        return result;
    }

    @Around("Pointcuts.getAnyMethodEmployeeController()")
    public Object onAnyUserControllerMethodCall(ProceedingJoinPoint joinPoint) throws Exception {

        Object result;
        
        String method = joinPoint.getSignature().getName();
        
        System.out.println("Method called: " + method);

        try {
            result = joinPoint.proceed();
        }
        catch (Throwable e) {
            System.out.println("Method " + method + " triggered ex: " + e.getMessage());
            throw new Exception(e);
            //e.printStackTrace();
        }

        System.out.println("Method "+ method +" finished with result: " + result);
        
        return result;
    }
}
