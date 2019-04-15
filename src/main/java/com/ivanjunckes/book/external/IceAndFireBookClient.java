package com.ivanjunckes.book.external;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Dependent
@RegisterRestClient
@Path("books")
public interface IceAndFireBookClient {

    @GET
    Response getBooks(@QueryParam("name") final String name);
}
