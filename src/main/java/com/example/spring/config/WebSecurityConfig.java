package com.example.spring.config;

import com.example.spring.mvc.seurity.CustomAccessDeniedHandler;
import com.example.spring.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// Расширенный конфиг
//@Configuration
//@EnableWebSecurity
//@Profile("!https")
/////@ComponentScan("com.example.spring.mvc")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
     
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    // private UserService userService;

    

    
    public WebSecurityConfig(/* boolean disableDefaults, */
                             @Autowired UserDetailsService userDetailsService,
                             @Autowired ObjectMapper objectMapper) {
        // super(disableDefaults);
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    // todo нужно ли это
    /* public void configureGlobalSecurity(@Autowired AuthenticationManagerBuilder auth,
                                        @Autowired AuthenticationProvider authenticationProvider,
                                        @Autowired PasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        auth.authenticationProvider(authenticationProvider);
    } */

    /*
    // todo нужно ли это
    // @Override
    protected void configure(AuthenticationManagerBuilder auth,
                             @Autowired AuthenticationManager authenticationManager) throws Exception {
        auth.parentAuthenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        // Stub to use a simple auth technique if authentication via DB is not implemented yet
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .and()
//                .withUser("test").password(passwordEncoder().encode("test"))
//                .roles("USER");
    }
    */
    
    // [!] этот бин лишний, если UserDetailsService реализован в виде отдельного класса CustomUserDetailsService
    /*
    @Bean
    @Override
    protected UserDetailsService userDetailsService(){
        // return super.userDetailsService(); // [!] стандартный вариант A
        // return new InMemoryUserDetailsManager() ; // [!] стандартный вариант B
        return userDetailsService; // кастомный вариант
    } */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/anonymous").anonymous()
                .antMatchers("/login*").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .anyRequest().authenticated() // after we permitted request to free realms, we restrict it. Request must be authenticated as users' one
                .and()
                .formLogin()
                .loginPage("/login") // the custom login page
                .loginProcessingUrl("/login") // the URL on which clients should post the login information (url to submit the username and password)
                .usernameParameter("userName") // the username parameter in the queryString
                .passwordParameter("password")
                .successHandler(this::loginSuccessHandler)
                .failureHandler(this::loginFailureHandler)
                .defaultSuccessUrl("/home", true) // the landing page after a successful login
                // the landing page after an unsuccessful login
                .failureUrl("/login?error=true")
                // .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/logout") // perform logout page
                // .logoutSuccessHandler(this::logoutSuccessHandler)
                // .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling() // default response if the client wants to get a resource unauthorized
        // .accessDeniedPage("/accessdenied")
        // .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
        ;
    }

    private void loginSuccessHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "Yayy you logged in!");

        System.out.println("Login Success");
    }

    private void loginFailureHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(), "Nopity nop!");

        System.out.println("Login Failure!");
    }

    private void logoutSuccessHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "Bye!");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
 
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }  

}

