package no.lau;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import no.lau.domain.KompoBackResource;
import no.lau.hello.api.Planet;
import no.lau.hello.api.Saying;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class KompoBackFullStackIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<KompoBackDropwizardConfiguration> RULE =
            new DropwizardAppRule<>(KompoBackApplication.class, ResourceHelpers.resourceFilePath("KompoBack-test.yml"));

    @Test
    public void getKompoBack() {
        Client client = JerseyClientBuilder.createClient();

        Response response = client.target(
                String.format("http://localhost:%d/KompoBack" + KompoBackResource.PATH, RULE.getLocalPort()))
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void postKompoBack() {
        Client client = JerseyClientBuilder.createClient();

        Response response = client.target(
                String.format("http://localhost:%d/KompoBack" + KompoBackResource.PATH, RULE.getLocalPort()))
                .request()
                .post(Entity.json(new Planet("Neptune", "Bad Santa")));

        assertThat(response.getStatus()).isEqualTo(200);
        Saying saying = response.readEntity(Saying.class);
        assertThat(saying.getContent()).isEqualTo("Hello Bad Santa on planet Neptune");
    }

}
