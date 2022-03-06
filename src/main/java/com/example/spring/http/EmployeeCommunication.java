package com.example.spring.http;

import com.example.spring.dto.Employee;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// больше пхоже на Communication-сервис
@Component
@Log
public class EmployeeCommunication {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/employee/v1/";

    @Autowired
    public EmployeeCommunication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // todo переделать чтобы возвращал другую ДТОшку
    public List<Employee> getAll() {

        ResponseEntity<List<Employee>> response = restTemplate.exchange(baseUrl + "all",
                                                                        HttpMethod.GET,
                                                                        null,
                                                                        new ParameterizedTypeReference<>() {
                                                                        });
        List<Employee> users = response.getBody();

        return users;
    }
 
    // [!] НЕ работает
    public Employee getOne(Long id) {

        Employee result = null;
        try {
            result = restTemplate.getForObject(baseUrl + id,
                                                        Employee.class);
        } catch (Throwable t){
            // log.error
            System.out.println(t.getMessage());
        }
        finally {
            return result;
        }        
    }

    public void saveOne(Employee user) {

    }

    public void delete(Long id) {

    }
}
