package com.example.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
//@EnableWebMvc
//@ComponentScan({"com.example.spring"})
public class LocalizationConfig implements WebMvcConfigurer {

    /**
     * The localeResolver is used to resolve user locale data.
     * The CookieLocaleResolver object is used to save user locale information in browser cookie.
     * This way user locale info can be transferred between request.
     * If user disable cookie then you can use SessionLocaleResolver instead.
     */
    @Bean(name = "localeResolver")
    @Primary
    public LocaleResolver getLocaleResolver()
    {
        if (false)
        {
            return getCookieLocaleResolver();
        }

        return getSessionLocaleResolver();
    }

    /**
     * For case when cookie is acceptable
     */
    public LocaleResolver getCookieLocaleResolver()
    {
        // Create a CookieLocaleResolver object.
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        // Set cookie name
        localeResolver.setCookieName("cookie-locale-info");
        // Set default locale value.
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        // Set cookie max exist time.
        localeResolver.setCookieMaxAge(3600);

        return localeResolver;
    }

    /**
     * For the case when user disabled cookie
     */
    public SessionLocaleResolver getSessionLocaleResolver()
    {
        // Create a SessionLocaleResolver object.
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        // Set default locale in session.
        localeResolver.setDefaultLocale(Locale.ENGLISH);

        return localeResolver;
    }

    /**
     * messageSource bean is spring built-in bean name which will manipulate internationalization messages.
     * All message files is saved in src/main/resources/config/ folder, if the config folder do not exist, you need to create it first by hand.
     * Each message file is a properties file, the file base name is messages and suffix with the language or country ISO code, such as messages_en, messages_zh_cn etc.
     */
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getMessageSource()
    {
        ReloadableResourceBundleMessageSource ret = new ReloadableResourceBundleMessageSource();

        // Set the base name for the messages properties file like "classpath:i18n/messages"
        ret.setBasename("classpath:i18n/messages");

        ret.setCacheSeconds(1);

        ret.setUseCodeAsDefaultMessage(true);

        ret.setDefaultEncoding("utf-8");

        return ret;
    }

    /**
     * The LocaleChangeInterceptor is a interceptor that will intercept user locale change by a parameter value.
     * For example, if we set the locale change parameter name is locale, then request url http://localhost:8088/index.jsp?locale=en will change
     * user locale to en and display messages in src/main/resources/i18n/messages_en.yaml
     * We need to add an interceptor bean that will switch to a new locale based on the value of the lang parameter appended to a request
     * This bean needs to be added to the application's interceptor registry
     */
    @Bean(name = "localeChangeInterceptor")
    public LocaleChangeInterceptor getLocaleChangeInterceptor()
    {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * We need to register above locale interceptor in spring then
     * it will intercept user request url and parse out the lang query string to get user request locale.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(getLocaleChangeInterceptor());
    }
}
