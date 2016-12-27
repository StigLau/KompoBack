package no.lau.spring;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringContextLoaderListener implements ServletContextListener {
    private final AnnotationConfigWebApplicationContext context;

    public SpringContextLoaderListener(AnnotationConfigWebApplicationContext context) {
        this.context = context;
    }

    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,context);
        context.setServletContext(sce.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
