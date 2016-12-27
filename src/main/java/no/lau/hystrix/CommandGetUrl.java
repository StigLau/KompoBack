
package no.lau.hystrix;

import java.net.URI;

public class CommandGetUrl extends BaseHystrixHttpGetCommand<String> {
    public CommandGetUrl(URI serviceUri) {
        super(serviceUri, null, null);
    }

    public CommandGetUrl(URI serviceUri, String username, String password) {
        super(serviceUri, username, password);
    }


}
