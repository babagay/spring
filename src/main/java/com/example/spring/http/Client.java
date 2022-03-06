package com.example.spring.http;

import com.example.spring.config.AppConfig;
import com.example.spring.dto.Employee;
import com.example.spring.mvc.controller.EmployeeController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * тестирование REST-приложения HTTP-клиентом
 * WebMvc-приложение д.б. запущено
 */
public class Client {

    // [!] при попытке запуска веб-приложения просто через main валится ошибка  No ServletContext set
    // вывод: web приложение нельзя запустить без сервлет-контейнера 
    // private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private final static EmployeeCommunication employeeRestService = context.getBean("employeeCommunication", EmployeeCommunication.class);

    public static void main(String[] args) {
      
        // getEmployeeAllTest();
        getEmployeeOneTest();

  
        
        // https://www.utilities-online.info/base64
        // закодировать
        // userName:admin,password:admin
        // и отправить заголовок
        // Authorization: Basic dXNlck5hbWU6YWRtaW4scGFzc3dvcmQ6YWRtaW4=
        // Либо переключиться с WebSecurityConfig на SecurityConfig, который пускает все запросы
    }

    // вообще , здесь нужна другая ДТО шка - com.example.spring.dto.Employee
    // [!] без авторизации валится. Нужно передать в заголовках auth данные
    private static void getEmployeeAllTest(){
        List<Employee> users = employeeRestService.getAll();
        System.out.println("RRR " + users.get(0).getName());
    }

    private static void getEmployeeOneTest(){
        Long id = 1L;
        Employee user = employeeRestService.getOne(id);

        System.out.println(user.getName() + " " + user.getSurname());
    }
}
