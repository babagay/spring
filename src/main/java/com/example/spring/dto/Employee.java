package com.example.spring.dto;
 
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Employee.class)
public class Employee {

    private Long id;
    private String name;
    private String surname;
    private UserDetails details;
    private Department department;
    
}
