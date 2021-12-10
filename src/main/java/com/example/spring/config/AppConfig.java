package com.example.spring.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * Конфигурация с помощью джава кода
 * Пример создания контекста - в TestConfig
 */
@Configuration // указание на то, что это конфигурационный класс
@ComponentScan("com.example.spring") // пакет, где искать бины
//@PropertySource("classpath:application.yml") // с этим не заводится
@PropertySource("classpath:application.properties") // рабочий источник пропертей
@EnableAspectJAutoProxy // включить АОП
@EntityScan(basePackages = {"com.example.spring.db"})
//@EnableWebMvc
// EnableConfigurationProperties
// ConfigurationProperties
public class AppConfig {


}
