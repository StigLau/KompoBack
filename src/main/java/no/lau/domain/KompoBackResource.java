package no.lau.domain;

import no.lau.hello.api.Planet;
import no.lau.hello.api.Saying;
import no.lau.domain.counter.CounterService;
import java.util.Optional;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import static net.logstash.logback.argument.StructuredArguments.*;

@Controller
@Path(KompoBackResource.PATH)
@Api(KompoBackResource.PATH)
@Produces({"application/json"})
public class KompoBackResource {
    private final static Logger log = LoggerFactory.getLogger(KompoBackResource.class);
    public static final String PATH = "/no/lau/hello";

    private final String template;
    private final String defaultName;

    private final CounterService counterService;

    @Configure
    public KompoBackResource(@Configuration String template, @Configuration String defaultName, CounterService counterService) {
        this.template = template;
        this.defaultName = defaultName;
        this.counterService = counterService;
        log.debug("{} Initialized with {}, {}", KompoBackResource.class.getSimpleName(), kv("template", template), kv("defaultName", defaultName));
    }

    @Timed
    @GET
    @ApiOperation("Endpoint that will respond with hello and use the name provided as request parameter if any")
    public Saying hello(@QueryParam("name") @ApiParam(defaultValue = "Mr. Smith") Optional<String> name) {
        log.trace("{} {} {}", v("method", HttpMethod.GET), v("path", KompoBackResource.PATH), kv("name", name.orElse("null")));
        final String value = String.format(template, name.orElse(defaultName));
        log.trace("{}", kv("value", value));
        Saying saying = new Saying(counterService.next(), value);
        log.trace("{}", fields(saying));
        return saying;
    }

    @Timed
    @POST
    @ApiOperation("Post planetName and yourName and be greeted.")
    @Consumes({"application/json"})
    public Saying hello(Planet planet) {
        log.trace("{} {} {}", v("method", HttpMethod.POST), v("path", KompoBackResource.PATH), fields(planet));
        final String value = "Hello " + planet.getYourName() + " on planet " + planet.getPlanetName();
        log.trace("{}", kv("value", value));
        Saying saying = new Saying(counterService.next(), value);
        log.trace("{}", fields(saying));
        return saying;
    }
}
