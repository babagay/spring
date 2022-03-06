package com.example.spring.dto;

import com.example.spring.db.EmployeeDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Map DTO to Bean or Entity
 */
public class Mapper {
 
    public static List<Employee> employeeEntityCollectionToDTO(Collection<com.example.spring.db.Employee> users) {

        List<Employee> result = null;

        if (users != null) {
            result = users.stream().map(Mapper::employeeEntityToDTO)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public static Employee employeeEntityToDTO(com.example.spring.db.Employee user) {

        Employee dto = null;

        if (user != null) {
            dto = new Employee();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setSurname(user.getSurname());

            dto.setDepartment(departmentEntityToDTO(user.getDepartment()));
            dto.setDetails(userDetailsEntityToDTO(user.getDetails()));
        }

        return dto;
    }

    public static Department departmentEntityToDTO(com.example.spring.db.Department department) {
        Department dto = null;

        if (department != null) {
            dto = new Department();
            dto.setId(department.getId());
            dto.setName(department.getName());
            dto.setMaxSalary(department.getMaxSalary());
            dto.setMinSalary(department.getMinSalary());
        }

        return dto;
    }

    public static UserDetails userDetailsEntityToDTO(EmployeeDetails details) {
        UserDetails dto = null;

        if (details != null) {
            dto = new UserDetails();
            dto.setCity(details.getCity());
            dto.setEmail(details.getEmail());
            dto.setPhoneNumber(details.getPhoneNumber());
            dto.setId(details.getId());
        }

        return dto;
    }
}
