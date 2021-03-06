package no.lau.kompoback;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import no.lau.kompoback.api.KompostEditBackendResource;
import no.lau.kompoback.domain.Komposition;
import no.lau.kompoback.hello.kompobackResource;
import no.lau.kompoback.hello.api.Planet;
import no.lau.kompoback.hello.api.Saying;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class kompobackFullStackIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<kompobackDropwizardConfiguration> RULE =
            new DropwizardAppRule<>(kompobackApplication.class, ResourceHelpers.resourceFilePath("kompoback-test.yml"));

    @Test
    public void getkompoback() {
        Client client = JerseyClientBuilder.createClient();

        Response response = client.target(
                String.format("http://localhost:%d/kompoback" + KompostEditBackendResource.PATH, RULE.getLocalPort()))
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void postkompoback() {
        Client client = JerseyClientBuilder.createClient();

        Komposition komposition = new Komposition();
        komposition.name = "KompoName";

        String postUrl = String.format("http://localhost:%d/kompoback" + KompostEditBackendResource.PATH, RULE.getLocalPort());
        Response response = client.target(
                postUrl)
                .request()
                .post(Entity.json(komposition));

        assertThat(response.getStatus()).isEqualTo(200);
        Saying saying = response.readEntity(Saying.class);
        assertThat(saying.getContent()).isEqualTo("Komposition KompoName 0 - 16");
    }

}
