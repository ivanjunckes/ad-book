package com.ivanjunckes.book;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.jaxrs.base.NoContentExceptionSupplier;
import com.fasterxml.jackson.jaxrs.base.ProviderBase;
import com.fasterxml.jackson.jaxrs.cfg.EndpointConfigBase;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.ivanjunckes.book.external.IceAndFireBook;
import com.ivanjunckes.common.ApiResult;
import com.ivanjunckes.common.JacksonJSONProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class BookResourceTest {

    private static final String SERVICE_ROOT = "/v1/books";

    @ArquillianResource
    private URL base;

    protected Client client;
    protected WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "bookapp.war")
                .addAsWebInfResource(new StringAsset("<beans/>"), "beans.xml")
                .addAsManifestResource(new ClassLoaderAsset("META-INF/persistence.xml"), "persistence.xml")
                .addAsResource("META-INF/microprofile-config.properties")
                .addClasses(Book.class, BookDao.class, BookResource.class, IceAndFireBook.class)
                .addPackage(JacksonJsonProvider.class.getPackage())
                .addPackage(ApiResult.class.getPackage());
        return webArchive;
    }

    @Before
    public void before() {
        this.client = ClientBuilder.newClient().register(JacksonJSONProvider.class);
        this.target = client.target(base.toExternalForm() + SERVICE_ROOT);
    }

    private Book buildBook() throws ParseException {
        Book book = new Book();
        book.setName("My First Book");
        book.setIsbn("123-3213243567");
        book.setAuthors(Arrays.asList("John Doe"));
        book.setNumberOfPages(350);
        book.setPublisher("Acme Books");
        book.setCountry("United States");
        book.setReleaseDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-08-01"));
        return book;
    }

    private void deleteBook(Integer id) {
        Response response = target
                .path("{id}")
                .resolveTemplate("id", id)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreate() throws ParseException {
        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(buildBook(), MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        JsonNode apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(201, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        assertNotNull(apiResult.get("data"));
        JsonNode book = apiResult.get("data").get(0);
        assertNotNull(book.get("name"));
        assertNotNull(book.get("isbn"));
        assertNotNull(book.get("authors"));
        assertNotNull(book.get("number_of_pages"));
        assertNotNull(book.get("publisher"));
        assertNotNull(book.get("country"));
        assertNotNull(book.get("release_date"));

        deleteBook(book.get("id").asInt());
    }

    @Test
    public void testRead() throws ParseException {
        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(buildBook(), MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        JsonNode apiResult = response.readEntity(JsonNode.class);
        JsonNode data = apiResult.get("data");

        checkReadByName();
        checkReadByCountry();
        checkReadByPublisher();
        checkReadByYear();
        checkReadByNameAndYear();

        deleteBook(data.get(0).get("id").asInt());
    }

    @Test
    public void testUpdate() throws ParseException {
        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(buildBook(), MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        JsonNode apiResult = response.readEntity(JsonNode.class);
        JsonNode book = apiResult.get("data").get(0);

        Book bookToBeUpdated = new Book();
        bookToBeUpdated.setCountry("Brazil");
        response = target
                .path("{id}")
                .resolveTemplate("id", book.get("id"))
                .request()
                .method("PATCH", Entity.entity(bookToBeUpdated, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        assertNotNull(apiResult.get("message"));
        assertEquals("The book My First Book was updated successfully", apiResult.get("message").asText());
        assertNotNull(apiResult.get("data"));

        book = apiResult.get("data").get(0);
        assertEquals("Brazil", book.get("country").asText());
        assertEquals("My First Book", book.get("name").asText());
        deleteBook(book.get("id").asInt());
    }

    @Test
    public void testDelete() throws ParseException {
        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(buildBook(), MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        JsonNode apiResult = response.readEntity(JsonNode.class);
        JsonNode book = apiResult.get("data").get(0);
        deleteBook(book.get("id").asInt());

        response = target
                .path("{id}")
                .resolveTemplate("id", book.get("id").asInt())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testShow() throws ParseException {
        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(buildBook(), MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        JsonNode apiResult = response.readEntity(JsonNode.class);
        JsonNode book = apiResult.get("data").get(0);

        response = target
                .path("{id}")
                .resolveTemplate("id", book.get("id").asInt())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        assertNotNull(apiResult.get("data"));
        book = apiResult.get("data").get(0);
        assertNotNull(book.get("name"));
        assertNotNull(book.get("isbn"));
        assertNotNull(book.get("authors"));
        assertNotNull(book.get("number_of_pages"));
        assertNotNull(book.get("publisher"));
        assertNotNull(book.get("country"));
        assertNotNull(book.get("release_date"));

        deleteBook(book.get("id").asInt());

        response = target
                .path("{id}")
                .resolveTemplate("id", book.get("id").asInt())
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
    }

    private void checkReadByName() {
        Response response;
        response = target
                .queryParam("name", "My First Book")
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
        JsonNode data = apiResult.get("data");
        assertTrue(data.size() == 1);

        response = target
                .queryParam("name", "Wrong Name")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        data = apiResult.get("data");
        assertTrue(data.size() == 0);
    }

    private void checkReadByCountry() {
        Response response;
        response = target
                .queryParam("country", "United States")
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
        JsonNode data = apiResult.get("data");
        assertTrue(data.size() == 1);

        response = target
                .queryParam("country", "Brazil")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        data = apiResult.get("data");
        assertTrue(data.size() == 0);
    }

    private void checkReadByPublisher() {
        Response response;
        response = target
                .queryParam("publisher", "Acme Books")
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
        JsonNode data = apiResult.get("data");
        assertTrue(data.size() == 1);

        response = target
                .queryParam("publisher", "Wrong Publisher")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        data = apiResult.get("data");
        assertTrue(data.size() == 0);
    }

    private void checkReadByYear() {
        Response response;
        response = target
                .queryParam("year", 2019)
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
        JsonNode data = apiResult.get("data");
        assertTrue(data.size() == 1);

        response = target
                .queryParam("year", 2022)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        data = apiResult.get("data");
        assertTrue(data.size() == 0);
    }

    private void checkReadByNameAndYear() {
        Response response;
        response = target
                .queryParam("name", "My First Book")
                .queryParam("year", 2019)
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
        JsonNode data = apiResult.get("data");
        assertTrue(data.size() == 1);

        response = target
                .queryParam("name", "My First Book")
                .queryParam("year", 2022)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        apiResult = response.readEntity(JsonNode.class);
        assertNotNull(apiResult);
        assertNotNull(apiResult.get("status_code"));
        assertEquals(200, apiResult.get("status_code").asInt());
        assertNotNull(apiResult.get("status"));
        assertEquals("success", apiResult.get("status").asText());
        data = apiResult.get("data");
        assertTrue(data.size() == 0);
    }
}