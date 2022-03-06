package com.example.spring.mvc.controller;

import com.example.spring.dto.Credentials;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    @GetMapping("/login")
    public ModelAndView login(@RequestParam @Nullable String error, Model model)
    {
        // logger.debug("Controller");
        System.out.println(error);
        
        if (Boolean.parseBoolean(error)){
            model.addAttribute("error", "ERROR");
        } else {
            model.addAttribute("error", "");
        }

        return new ModelAndView("login", "credentials", new Credentials());
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("credentials")
                                    Credentials credentials,
                        BindingResult result,
                        ModelMap model)
    {
//        User user = new User();
//        user.setUsername("test");
//        User usr = userService.findByObject(user);


        if (result.hasErrors())
        {
            return "login.error";
        }

        model.addAttribute("userName", credentials.getUserName());
        model.addAttribute("password", credentials.getPassword());


        return "home";
    }
}
