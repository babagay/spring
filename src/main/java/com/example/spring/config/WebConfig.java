package com.example.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

// https://www.baeldung.com/spring-boot-start
// https://www.baeldung.com/spring-mvc
// https://www.baeldung.com/spring-mvc-tutorial
 
/**
 *  [!] этот файл НЕ является рутовым конфигом для старта веб-приложения 
 *  и вообще, не является рабочим классом
 *  основной класс для web - com.example.spring.Application
 *  Через него стартует контейнер сервлетов
 *  С другой стороны, этот конфиг НЕ подходит для запуска через main без использования Tomcat
 */

@Configuration
@EnableWebMvc
//@WebAppConfiguration
@ComponentScan(basePackages = "com.example.spring")
@EnableAspectJAutoProxy
@EntityScan(basePackages = {"com.example.spring.db"})
//@PropertySource("classpath:application.properties")
@PropertySources({
        @PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:i18n/messages.yml"),
        @PropertySource(value = "classpath:application.properties")
})
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        System.out.println("======== "+ WebConfig.class.getName() + ": addViewControllers========");
        registry.addViewController("/").setViewName("home");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
        // registry.addResourceHandler("/WEB-INF/static/**").addResourceLocations("/assets/"); не работает
    }

    @Bean
    public ViewResolver viewResolver() {
        // [A] variant for Jsp
        // InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        // viewResolver.setViewClass(JstlView.class);
        // viewResolver.setPrefix("/WEB-INF/");
        // viewResolver.setPrefix("classpath:/view/");
        //viewResolver.setSuffix(".jsp");


        // [B] Thymeleaf version
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(new UrlTemplateResolver());

        ThymeleafViewResolver bean = new ThymeleafViewResolver();
        bean.setTemplateEngine(templateEngine());
        bean.setCharacterEncoding("UTF-8");
        bean.setTemplateEngine(templateEngine);

        return bean;
    }

    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine templateEngine() {
        // SpringTemplateEngine automatically applies SpringStandardDialect and
        // enables Spring's own MessageSource message resolution mechanisms.
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        // SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        //templateResolver.setPrefix("classpath:templates/"); // OK
        templateResolver.setSuffix(".html");
        // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML); // cam be used "HTML5" instead
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    // альтернативное место для поиска темплейтов
    // не сработало
    @Bean
    public ClassLoaderTemplateResolver secondaryTemplateResolver() {
        ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
        secondaryTemplateResolver.setPrefix("classpath:templates/");
        secondaryTemplateResolver.setSuffix(".html");
        secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
        secondaryTemplateResolver.setCharacterEncoding("UTF-8");
        secondaryTemplateResolver.setOrder(1);
        secondaryTemplateResolver.setCheckExistence(true);

        return secondaryTemplateResolver;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
