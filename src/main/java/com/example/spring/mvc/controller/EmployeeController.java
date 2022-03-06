package com.example.spring.mvc.controller;

import com.example.spring.db.Employee;
import com.example.spring.db.EmployeeDetails;
import com.example.spring.dto.Mapper;
import com.example.spring.mvc.validator.EmailCustom;
import com.example.spring.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Log
@Controller
// @ControllerAdvice // the methods in an @ControllerAdvice apply globally to all controllers. you can add values in Model which will be identified as global
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String employeeHome(Model model) {

        Collection<Employee> users_ = employeeService.all();
        ArrayList<Employee> users = new ArrayList<>(users_); 
        model.addAttribute("users", users);

        return "employee/employee-home";
    }
    
   
    @GetMapping("askdetails")
    public String askEmployeeDetails(Model model) {
        model.addAttribute("employeeDTO", new EmployeeDTO());
        log.info("askdetails endpoint works"); 
        return "employee/ask-employee-details";
    }

    @GetMapping("showdetails")
    // если применять аннотацию ModelAttribute к методу, она воздействует на возвращамый объект - им становится EmployeeDTO dto
    // Поэтому, возврат строки  (имени шаблона) - return "show-employee-details" - уже не подходит
    // Вместе с тем, в качестве шаблона автоматически генерится строка templatepath/employee/showdetails.html,
    //      в кторую вклечены имя контроллера т имя эндпоинта
    @ModelAttribute("employeeDTO")
    public EmployeeDTO showEmployeeDetails(@RequestParam("name") @NonNull String name,
                                           Model model) {        
        Employee user = employeeService.findByUsername(name);

        EmployeeDTO dto = mapEmployeeToDTO(user); // этот объект попадет в шаблон

        return dto;

        // return "show-employee-details";
    }

    @PostMapping(consumes = {"application/x-www-form-urlencoded", "application/json", "application/*+json"}, produces = "text/html")
    public // @ResponseBody
    String saveEmployee(@Valid @ModelAttribute final EmployeeDTO dto, // у нас будет EmployeeDTO employeeDTO объект, автоматом привязанный к шаблону
                        final BindingResult bindingResult,
                        final ModelMap model) {
        // @RequestBody EmployeeDTO model
        // Model model 
        // HttpServletRequest request - OK
        // [see]: @ModelAttribute VS @RequestBody https://stackoverflow.com/questions/21824012/spring-modelattribute-vs-requestbody

        if (bindingResult.hasErrors() || !isPasswordValid(dto, bindingResult)) { // как добавить "no password set" в авто валидацию
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "employee/show-employee-details"; // или return "error"
        }
  
        employeeService.create(mapDTOtoEmployee(dto));
        
        model.clear(); // зачем?

        // return "show-employee-details"; // или редирект  
        
        return "redirect:/employee/showdetails?name=" + dto.getName();
    }

    private boolean isPasswordValid(@NonNull EmployeeDTO dto,BindingResult bindingResult) {

        if (dto.getPassword() == null || dto.getPasswordDouble() == null) {
            bindingResult.addError(new ObjectError("EmployeeDTO", "Password is empty"));
            return false;
        }

        if (!dto.getPassword().equals(dto.getPasswordDouble())) {
            bindingResult.addError(new ObjectError("EmployeeDTO", "Password is not equal its doubler"));
            return false;
        }

        return true;
    }

    private static Employee mapDTOtoEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setSurname(dto.getSurname());
        employee.setPassword(dto.getPassword());
        // dto.getPasswordDouble() equality check
        EmployeeDetails details = new EmployeeDetails();
        details.setCity(dto.getCity());
        details.setPhoneNumber(dto.getPhoneNumber());
        details.setEmail(dto.getEmail());

        employee.setDetails(details);

        return employee;
    }

    private static EmployeeDTO mapEmployeeToDTO(@NonNull Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName(employee.getName());
        dto.setSurname(employee.getSurname());

        Optional.ofNullable(employee.getDetails())
                .ifPresent(details -> {
                    dto.setEmail(details.getEmail());
                    dto.setPhoneNumber(details.getPhoneNumber());
                });
        return dto;
    }

    // [!] DTO используется чисто для проброса данных формы на этапе ввода нового юзера
    // DTO для проброса суествующего юера на UI - com.example.spring.dto.Employee
    @Data // аннотация от projectlombok.org 
    public static class EmployeeDTO {

        @Size(min = 4, max = 25, message = "Name should be 4 - 25 letters size")
        private String name;
        private String surname;
        private String password;
        private String passwordDouble;
        private String city;
        
        @Pattern(regexp = "\\d{3}-\\d{3}-\\d{2}-\\d{2}", message = "Phone should satisfy the pattern: xxx-AAA-BB-CC")
        private String phoneNumber;
        
        @EmailCustom(value = "^\\w{1,12}@\\w{1,12}\\.\\w{2,4}$") // кастомная аннотация для задания шаблона имейла
        private String email;
    }
}
