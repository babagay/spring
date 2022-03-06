package com.example.spring.service;

import com.example.spring.db.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;


@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {


    // private final LocalContainerEntityManagerFactoryBean entityManagerFactory;
    // private final LocalSessionFactoryBuilder hibernate5AnnotatedSessionFactory; 
    private final EmployeeService userService;
    private final DataSource dataSource;
    //private final PasswordEncoder passwordEncoder;

   
    @Autowired
    public CustomUserDetailsService(//LocalContainerEntityManagerFactoryBean entityManagerFactory,
                                    //LocalSessionFactoryBuilder hibernate5AnnotatedSessionFactory,
                                    EmployeeService userService,
                                    DataSource dataSource
                                    // todo как его заинжектить
                                   // PasswordEncoder passwordEncoder // The dependencies of some of the beans in the application context form a cycle:
                                   ) {
        // this.entityManagerFactory = entityManagerFactory;
        // this.hibernate5AnnotatedSessionFactory = hibernate5AnnotatedSessionFactory; 
        this.userService = userService;
        this.dataSource = dataSource;
        //this.passwordEncoder = passwordEncoder;
    }
 
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      
        // [!] можно сгенерить юзера так
        // UserDetails u = User.builder()
        //        .username(employee.getName())
        //        .password(employee.getName())
        //        .roles("ADMIN").build();
        
        // [!] можно взять юзера через менеджера рользователей
        // JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);
        // UserDetails usr = userManager.loadUserByUsername(username);

        // [!] можно взять юзера вручную
        Employee employee = userService.findByUsername(username);

        if (employee == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }

        //  new BCryptPasswordEncoder(12).encode('admin')
        
        // [!] вернуть юзера, понятного для spring security
        return
                new org.springframework.security.core.userdetails.User(
                        employee.getName(),
                        //"admin", //passwordEncoder.encode("admin"), // это вызывает циклическую зависимость
                        // "$2a$12$RD3s4943.JuuwGs813i5UOaGH6y6VcKjutXM3jKo1gZ0sI3GUh6KO", // 'admin'
                        employee.getPassword(),
                        getGrantedAuthorities(employee)
                ); // new principal
    }

    private Set<GrantedAuthority> getGrantedAuthorities(Employee user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

//        if (user.getRoles() != null)
//        {
//            for (Role role : user.getRoles())
//            {
//                authorities.add(new SimpleGrantedAuthority(role.getTitle()));
//            }
//        }

        return authorities;
    } 
}
