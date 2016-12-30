package no.lau.kompoback;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackageClasses = kompobackSpringConfiguration.class)
@ImportResource("classpath:constretto/spring-constretto.xml")
public class kompobackSpringConfiguration {
}
