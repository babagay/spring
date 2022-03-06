package com.example.spring;

import com.example.spring.config.YamlConfig;
import com.example.spring.db.Employee;
import com.example.spring.db.EmployeeRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

import static com.example.spring.config.AppConstants.*;

// todo можно попробовать по этой статье 
// https://www.baeldung.com/spring-boot-start
// https://www.baeldung.com/spring-mvc
// https://www.baeldung.com/spring-mvc-tutorial

// [!] видимо, второй файл - AppConfig - является альтернативным способом запуска
// и НЕ нужен

// Используем этот класс для запуска web-приложения
// [1] https://www.baeldung.com/spring-mvc-tutorial
// [2] https://www.baeldung.com/spring-boot-start

@SpringBootApplication(scanBasePackages = {
        "com.example.spring",
        "com.example.spring.db",
        "com.example.spring.config"
}
//, scanBasePackageClasses = com.example.spring.config.DataConfig
)
//@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = { //
//        DataSourceAutoConfiguration.class, //
//        DataSourceTransactionManagerAutoConfiguration.class, //
//        HibernateJpaAutoConfiguration.class })
// @EntityScan(basePackages = {"com.example.spring.db"})
//@ComponentScan(basePackages = {"com.example.spring"}) // пакеты, где искать бины
// @EnableJpaRepositories("com.example.spring.db")
// @PropertySources({
//        //@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:i18n/messages.yml"),
//        @PropertySource(value = "classpath:application.properties")
// })
public class Application implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
 
    private final Environment env;
 
    private final YamlConfig myConfig;

    @Autowired
    public Application(YamlConfig myConfig, Environment env) {
        this.myConfig = myConfig;
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("======== "+ env.getRequiredProperty(APPLICATION_NAME) + " from " + Application.class.getName() +  " RUN ==========");
        // System.out.println("using environment: " + myConfig.getEnvironment());
        // System.out.println("name: " + myConfig.getName());
        // System.out.println("enabled:" + myConfig.isEnabled());
        // System.out.println("servers: " + myConfig.getServers());
    }

    /*@Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // See: application.properties
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        System.out.println("## getDataSource: " + dataSource);
        return dataSource;
    }
*/
    // Parameter 0 of method openEntityManagerInViewInterceptorConfigurer in org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration$JpaWebConfiguration required a single bean, but 2 were found:
    //	- entityManagerFactory: defined by method 'entityManagerFactory' in class path resource [com/example/spring/config/DataConfig.class]
    //	- sessionFactory: defined by method 'getSessionFactory' in com.example.spring.Application 
    // Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
   /*
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
        Properties properties = new Properties();
        // See: application.properties
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("current_session_context_class", //
                       env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
        // Fix Postgres JPA Error:
        // Method org.postgresql.jdbc.PgConnection.createClob() is not yet implemented.
        // properties.put("hibernate.temp.use_jdbc_metadata_defaults",false);
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        // Package contain entity classes
        factoryBean.setPackagesToScan(new String[] { "" });
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();
        //
        SessionFactory sf = factoryBean.getObject();
        System.out.println("## getSessionFactory: " + sf);
        return sf;
    }
    */
     
    
}
