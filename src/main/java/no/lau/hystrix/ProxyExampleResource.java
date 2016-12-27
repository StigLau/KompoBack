
package no.lau.hystrix;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.v;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by baardl on 2016-10-04.
 */
@Controller
@Path(ProxyExampleResource.PATH)
@Api(ProxyExampleResource.PATH)
@Produces({"application/json"})
public class ProxyExampleResource {
    private static final Logger log = getLogger(ProxyExampleResource.class);
    public static final String PATH = "/proxy";
    private static final String REQUEST_URL = "request_url";
    private static final String RESPONSE = "response";

    @Timed
    @GET
    @ApiOperation("Endpoint will proxy request, and report to Hystrix.")
    public Map<String,String> proxy(@QueryParam("url") String url) {
        log.trace("{} {} {}", v("method", HttpMethod.GET), v("path", PATH));

        Map<String,String> values = new HashMap<>();
        values.put(REQUEST_URL, url);
        URI uri = URI.create(url);
        String response = new CommandGetUrl(uri).execute();
        values.put(RESPONSE, response);
        return values;
    }


}
