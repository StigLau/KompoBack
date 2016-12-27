
package no.lau.hystrix;

import com.github.kevinsawicki.http.HttpRequest;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.slf4j.Logger;

import no.lau.hystrix.util.StringConv;

import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;


public class BaseHystrixHttpGetCommand<R> extends HystrixCommand<R> {
    private static final Logger log = getLogger(BaseHystrixHttpGetCommand.class);
    private final URI serviceUri;
    private final String password;
    private final String username;
    private final String contentType;

    public BaseHystrixHttpGetCommand(URI serviceUri, String username, String password) {
        super(HystrixCommandGroupKey.Factory.asKey("Device"));
        this.username = username;
        this.password = password;
        this.serviceUri = serviceUri;
        contentType = "application/json";
    }

    public BaseHystrixHttpGetCommand(URI serviceUri, String contentType, String username, String password) {
        super(HystrixCommandGroupKey.Factory.asKey("Device"));
        this.username = username;
        this.password = password;
        this.serviceUri = serviceUri;
        this.contentType = contentType;

    }

    @Override
    protected R run() throws Exception {

        String uriString = serviceUri.toString();
        log.trace("UriString {}", uriString);
        HttpRequest request = HttpRequest.get(uriString).basic(username, password).accept(contentType);
        byte[] responseBody = request.bytes();
        int statusCode = request.code();
        String responseAsText = StringConv.UTF8(responseBody);

        switch (statusCode) {
            case java.net.HttpURLConnection.HTTP_OK:
                onCompleted(responseAsText);
                return dealWithResponse(responseAsText);
            default:
                onFailed(responseAsText, statusCode);
                return dealWithFailedResponse(responseAsText, statusCode);
        }
    }
    private void onFailed(String responseBody, int statusCode) {
        log.trace( "Unexpected response from {}. Status code is {} content is {} ", serviceUri, String.valueOf(statusCode) + responseBody);
    }

    private void onCompleted(String responseBody) {
        log.debug("ok: " + responseBody);
    }

    @SuppressWarnings("unchecked")
    private R dealWithResponse(String response){
        return (R)response;
    }

    @Override
    protected R getFallback() {
        log.warn("fallback - serviceUri={}", serviceUri.toString());
        return null;
    }

    private R dealWithFailedResponse(String responseBody, int statusCode) {
        log.warn("Failed to fetch data from {}. Status {}, response {}", serviceUri, statusCode, responseBody);
        return null;
    }
}
