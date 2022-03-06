package com.example.spring.mvc.rest;

import com.example.spring.db.Employee;
import com.example.spring.dto.Mapper;
import com.example.spring.service.EmployeeService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log
@RestController(value = "employeeRestController")
@RequestMapping("/employee/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // todo 
    // https://www.baeldung.com/jackson-object-mapper-tutorial
    // https://www.baeldung.com/jackson
    // https://spring-projects.ru/guides/rest-service/
    @GetMapping(value = "all") // produces = "application/json"
    // @ResponseBody
    // это приведет к автоматической конвертации в json. Когда Jackson 2 в classpath, MappingJackson2HttpMessageConverter из Spring выбирается автоматически для конвертации экземпляра результирующего объекта
    // Проблема: преобразование в JSON генерит бесконечный фрактальный объект
    public ResponseEntity<List<com.example.spring.dto.Employee>> employeeAll() {

        List<com.example.spring.dto.Employee> result = new ArrayList<>();

        Collection<Employee> users_ = employeeService.all();
        ArrayList<Employee> emps = new ArrayList<>(users_);
        if (emps.size() > 0) {
            result = Mapper.employeeEntityCollectionToDTO(emps);
            // result.add(Mapper.employeeEntityToDTO(emps.get(0)));  OK
        }
        
        /*
        ObjectMapper mapper = new ObjectMapper();
        String us = null;
        try {
            us = mapper.writeValueAsString(new ArrayList<>());
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return us; */

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<com.example.spring.dto.Employee> employeeOne(@PathVariable Long id) {
        com.example.spring.dto.Employee result;
        result = employeeService.getById(id)
                .map(Mapper::employeeEntityToDTO)
                .orElse(null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
