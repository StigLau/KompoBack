
package no.lau.hystrix;

import com.github.kevinsawicki.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CommandPostWithCertificate extends BaseHttpPostHystrixCommand<X509Certificate> {
    private final static Logger log = LoggerFactory.getLogger(CommandPostWithCertificate.class);
    public static final int HYSTRIX_TIMEOUT = 6000;
    public static final String HYSTRIX_GROUP_ID = "KompoBack";

    String herID; //FIXME Totto
    private final URI uri;
    private final String username;
    private final String password;
    private final String hystrixGroupId;
    private final int hystrixTimeout;

    public CommandPostWithCertificate(URI serviceUri) {
        this(serviceUri,null, null);
    }

    public CommandPostWithCertificate(URI serviceUri, String username, String password) {
        this(serviceUri, username, password, HYSTRIX_GROUP_ID, HYSTRIX_TIMEOUT);
    }

    public CommandPostWithCertificate(URI serviceUri, String username, String password, String hystrixGroupId, int hystrixTimeout) {
        super(serviceUri, hystrixGroupId, hystrixTimeout);
        this.uri = serviceUri;
        this.username = username;
        this.password = password;
        this.hystrixGroupId = hystrixGroupId;
        this.hystrixTimeout = hystrixTimeout;
    }



//    public CommandPostWithCertificate(String uri, String herID) {
//
//        super(URI.create(uri), "NHN", 6000);
//        this.uri = uri;
//        this.herID = herID;
//    }


    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
        String authString = username + ":" + password;
        log.trace("auth string: " + authString);
        byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        return request.header("SOAPAction", "http://register.nhn.no/CommunicationParty/ICommunicationPartyService/GetCertificateDetailsForEncryption").authorization("Basic " + authStringEnc).contentType("text/xml").send(getSOAPEnvelope(herID).toString());
    }
    // Authorization:Basic cGFzaWVudHNreTpRNlpHUnMybVhmRWFmWA==

    @Override
    protected String getTargetPath() {
        return "";
    }


    @Override
    protected X509Certificate dealWithResponse(String response) {
        try {
            byte[] certArray = ("-----BEGIN RSA PRIVATE KEY-----\n" + response.substring(response.indexOf("<a:Certificate>") + "<a:Certificate>".length(), response.indexOf("</a:Certificate>")) + "-----END RSA PRIVATE KEY-----\n").getBytes("UTF-8");
            X509Certificate cert = null;
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(certArray);
            cert = (X509Certificate) certFactory.generateCertificate(in);
            in.close();
            log.trace("Cert:" + cert.getIssuerDN());
            return cert;
        } catch (Exception e) {
            log.warn("Illegal certificate received from NHN", e);
        }
        return null;
    }


    private String getSOAPEnvelope(String herID) {
        String soapEnvelope =
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "    <Body>\n" +
                        "        <GetCertificateDetailsForEncryption xmlns=\"http://register.nhn.no/CommunicationParty\">\n" +
                        "            <herId>" + herID + "</herId>\n" +
                        "        </GetCertificateDetailsForEncryption>\n" +
                        "    </Body>\n" +
                        "</Envelope>";
        return soapEnvelope;

    }
}
