package no.lau.kompoback.api;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.lau.kompoback.domain.Komposition;
import no.lau.kompoback.domain.Segment;
import no.lau.kompoback.domain.counter.CounterService;
import no.lau.kompoback.hello.api.Saying;
import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.*;

@Controller
@Path(KompostEditBackendResource.PATH)
@Api(KompostEditBackendResource.PATH)
@Produces({"application/json"})
public class KompostEditBackendResource {
    private final static Logger log = LoggerFactory.getLogger(KompostEditBackendResource.class);
    public static final String PATH = "/no/lau/kompo";

    private final String template;
    private final String defaultName;

    private final CounterService counterService;

    @Configure
    public KompostEditBackendResource(@Configuration String template, @Configuration String defaultName, CounterService counterService) {
        this.template = template;
        this.defaultName = defaultName;
        this.counterService = counterService;
        log.debug("{} Initialized with {}, {}", KompostEditBackendResource.class.getSimpleName(), kv("template", template), kv("defaultName", defaultName));
    }

    @Timed
    @GET
    @ApiOperation("Endpoint that will respond with kompo and use the name provided as request parameter if any")
    public Komposition kompo(@QueryParam("identity") @ApiParam(defaultValue = "Komposition JSON") Optional<String> identity) {
        log.trace("{} {} {}", v("method", HttpMethod.GET), v("path", KompostEditBackendResource.PATH), kv("name", identity.orElse("null")));
        final String value = String.format(template, identity.orElse(defaultName));
        log.info("{}", kv("value", value));
        Komposition kompo = new Komposition();
        kompo.name="Kurt Bjarne";
        kompo.segments = new ArrayList<>();
        kompo.segments.add(new Segment("first one", 0, 16));
        kompo.segments.add(new Segment("Second", 16, 32));

        log.trace("{}", fields(kompo));
        return kompo;
    }

    @Timed
    @POST
    @ApiOperation("Post komposition and be greeted.")
    @Consumes({"application/json"})
    public Saying kompo(Komposition komposition) {
        log.trace("{} {} {}", v("method", HttpMethod.POST), v("path", KompostEditBackendResource.PATH), fields(komposition));
        String value = "\nKomposition: " + komposition.name;
        for (Segment segment : komposition.segments) {
            value += "\nSegment: " + segment.id + "- " + segment.end;
        }
        log.info("{}", kv("value ost", value));

        Saying saying = new Saying(counterService.next(), value);
        log.trace("{}", fields(saying));
        return saying;
    }
}