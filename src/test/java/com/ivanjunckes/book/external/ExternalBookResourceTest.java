package com.ivanjunckes.book.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.ivanjunckes.common.JacksonJSONProvider;
import org.apache.ziplock.maven.Mvn;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class ExternalBookResourceTest {

    private static final String SERVICE_ROOT = "/external-books";

    @ArquillianResource
    private URL base;

    protected Client client;
    protected WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        final Archive<?> war = Mvn.war();
        return (WebArchive) war;
    }

    @Before
    public void before() {
        this.client = ClientBuilder.newClient().register(JacksonJSONProvider.class);
        this.target = client.target(base.toExternalForm() + SERVICE_ROOT);
    }

    @Test
    public void testGetExternalBooksWithName() {
        Response response = target
                .queryParam("name", "A Game of Thrones")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        JsonNode apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        assertNotNull(apiResult.get("data"));
        JsonNode data = apiResult.get("data").get(0);
        assertNotNull(data.get("name"));
        assertNotNull(data.get("isbn"));
        assertNotNull(data.get("authors"));
        assertNotNull(data.get("number_of_pages"));
        assertNotNull(data.get("publisher"));
        assertNotNull(data.get("country"));
        assertNotNull(data.get("release_date"));
    }

    @Test
    public void testGetExternalBooksWithoutName() {
        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        JsonNode apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        assertNotNull(apiResult.get("data").size() > 1);
    }
}