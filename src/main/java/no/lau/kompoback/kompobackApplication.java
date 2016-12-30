package no.lau.kompoback;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import no.lau.kompoback.spring.SpringContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Map;

public class kompobackApplication extends Application<kompobackDropwizardConfiguration> {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[] {"server", "kompoback.yml"};
        }
        new kompobackApplication().run(args);
    }

    @Override
    public String getName() {
        return "kompoback";
    }

    @Override
    public void initialize(Bootstrap<kompobackDropwizardConfiguration> bootstrap) {
        bootstrap.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        bootstrap.addBundle(new AssetsBundle("/assets/index.html","/","index.html"));
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.addBundle(new SwaggerBundle<kompobackDropwizardConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(kompobackDropwizardConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(kompobackDropwizardConfiguration configuration,
                    Environment environment) {
        AnnotationConfigWebApplicationContext parent = createSpringParentContext(configuration);
        AnnotationConfigWebApplicationContext ctx = createSpringContext(parent);
        environment.servlets().addServletListeners(new SpringContextLoaderListener(ctx));
        addManaged(environment, ctx);
        addTasks(environment, ctx);
        addHealthChecks(environment, ctx);
        addResources(environment, ctx);
        addProviders(environment, ctx);
    }

    private AnnotationConfigWebApplicationContext createSpringParentContext(kompobackDropwizardConfiguration configuration) {
        AnnotationConfigWebApplicationContext parent = new AnnotationConfigWebApplicationContext();
        parent.refresh();
        parent.getBeanFactory().registerSingleton("configuration", configuration);
        parent.registerShutdownHook();
        parent.start();
        return parent;
    }

    private AnnotationConfigWebApplicationContext createSpringContext(AnnotationConfigWebApplicationContext parent) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setParent(parent);
        ctx.register(kompobackSpringConfiguration.class);
        ctx.refresh();
        ctx.registerShutdownHook();
        ctx.start();
        return ctx;
    }

    private void addManaged(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Managed> managed = ctx.getBeansOfType(Managed.class);
        for(Map.Entry<String,Managed> entry : managed.entrySet()) {
            environment.lifecycle().manage(entry.getValue());
        }
    }

    private void addHealthChecks(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, HealthCheck> healthChecks = ctx.getBeansOfType(HealthCheck.class);
        for(Map.Entry<String,HealthCheck> entry : healthChecks.entrySet()) {
            environment.healthChecks().register(entry.getKey(), entry.getValue());
        }
    }

    private void addResources(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        for(Map.Entry<String,Object> entry : resources.entrySet()) {
            environment.jersey().register(entry.getValue());
        }
    }

    private void addTasks(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Task> tasks = ctx.getBeansOfType(Task.class);
        for(Map.Entry<String,Task> entry : tasks.entrySet()) {
            environment.admin().addTask(entry.getValue());
        }
    }

    private void addProviders(Environment environment, AnnotationConfigWebApplicationContext ctx) {
        Map<String, Object> providers = ctx.getBeansWithAnnotation(Provider.class);
        for(Map.Entry<String,Object> entry : providers.entrySet()) {
            environment.jersey().register(entry.getValue());
        }
    }
}
