package com.example.spring.mvc.controller;

import com.example.spring.db.Employee;
import com.example.spring.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    
    EmployeeService employeeService;

    @Autowired
    public HomeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getHome(Model model, Authentication authentication) {
        
        model.addAttribute("authName", "=authName=");
        
        return "home";
    }
    
    @GetMapping("/details")
    public String getDetails(HttpServletRequest request, Model model){
        String name = request.getParameter("name");
        
        int id = 0;

        Optional.ofNullable( employeeService.findByUsername(name))
                        .ifPresent(user -> {
                            model.addAttribute("authName", user.getName());
                            model.addAttribute("id", id);
                            model.addAttribute("userSurname", user.getSurname());
                        });
         
        return "home";
    }
}
