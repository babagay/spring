package com.example.spring.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация с помощью джава кода
 * Пример создания контекста - в TestConfig
 * [!] Конфиг-класс использовался в тестах на этапе тестирования хибернейта и аспектов
 * длее, введен в эксплуатацию WebConfig
 * [!] Это обычное приложение [командной строки],
 * НЕ предназначенное для запуска сервлетов
 * Если вставить сюда @EnableWebMvc, будет ошибка IllegalStateException: No ServletContext set
 * Другая проблема связана с @ComponentScan
 * Если указать коренб пакетов, спринг просканирует все папки и найдет web-конфигурацию, из-за чего отвалится с ошибкой No ServletContext set
 *
 * @ PropertySource("classpath:application.yml") // с этим не заводится
 * @ EnableConfigurationProperties
 * @ ConfigurationProperties
 */
@Configuration // указание на то, что это конфигурационный класс
//@EnableAutoConfiguration
//  excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.spring\\.config\\.Web*")}
@ComponentScan(basePackages = {
                        "com.example.spring.service",
                        "com.example.spring.introduction",
                        "com.example.spring.http",
                        "com.example.spring.db",
                        "com.example.spring.aop",
                        "com.example.spring.aspect",
                        "com.example.spring.config"
                        }, // пакет, где искать бины
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = {
                        com.example.spring.config.WebConfig.class,
                        com.example.spring.config.WebAppInitializer.class,
                        com.example.spring.config.WebSecurityConfig.class,
                        com.example.spring.config.RootApplicationConfig.class,
                        com.example.spring.config.YamlConfig.class,
                        com.example.spring.config.YamlPropertySourceFactory.class,
                        com.example.spring.config.LocalizationConfig.class,
                        com.example.spring.config.SecurityConfig.class,
                        com.example.spring.Application.class
                })}
)
@PropertySource("classpath:application.properties") // рабочий источник пропертей
@EnableAspectJAutoProxy // включить АОП
@EntityScan(basePackages = {"com.example.spring.db"})
public class AppConfig {

    //    @Bean(name = "mvcHandlerMappingIntrospector")
//    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
//        return new HandlerMappingIntrospector();
//    }
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
