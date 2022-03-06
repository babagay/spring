package com.example.spring.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * [?] для чего нужен этот конфиг, не понял
 * 
 * Юзаем WebApplicationInitializer в случае использования чистой конфигурации в джава-коде (без использования xml-конфигов)
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/WebApplicationInitializer.html
 * https://www.baeldung.com/spring-web-contexts
 * 
 * public class AnnotationsBasedApplicationInitializer 
 * 
 * [!] насколько я понял, надо прописывать подключение этого файла в WEB-INF/web.xml
 *      и также юзать WEB-INF/applicationContext.xml
 */
public class WebAppInitializer /*implements WebApplicationInitializer*/ {

   
    
//    @Override
//    public void onStartup(final ServletContext sc) throws ServletException {
//
//        AnnotationConfigWebApplicationContext root =
//                new AnnotationConfigWebApplicationContext();
//
//        root.scan("com.example.spring.mvc");
//        sc.addListener(new ContextLoaderListener(root));
//
//        ServletRegistration.Dynamic appServlet = sc.addServlet("mvc", new DispatcherServlet(new GenericWebApplicationContext()));
//        appServlet.setLoadOnStartup(1);
//        appServlet.addMapping("/");
//    }

    protected WebApplicationContext createRootApplicationContext(){
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootApplicationConfig.class);
        return rootContext;
    }
    
    /*
    @Override
    public void onStartup(final ServletContext context) throws ServletException
    {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        //  XmlWebApplicationContext rootContext = new XmlWebApplicationContext();

        // Define the scope
        rootContext.scan("com.example.spring");

        // Manage the lifecycle of the root application context
        context.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        // [A]
        // AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        // dispatcherContext.register(DispatcherConfig.class);
        // [B]
        GenericWebApplicationContext dispatcherContext = new GenericWebApplicationContext();

        // Register and map the dispatcher servlet
        // [!] servletName = "mvc"
        ServletRegistration.Dynamic dispatcher = context.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
     */
    
    /*
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return Stream.of(AppConfig.class).toArray(size -> new Class<?>[size]);
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return Stream.of(SpringMVCRestConfig.class).toArray(size -> new Class<?>[size]);
    }

    @Override
    protected Filter[] getServletFilters() {
        return Stream.of(new DelegatingFilterProxy()).toArray(size -> new Filter[size]);
    }

    //@Override
    protected String[] getServletMappings() {
        return new String[] { "/api/*" };
    }
*/   
}
