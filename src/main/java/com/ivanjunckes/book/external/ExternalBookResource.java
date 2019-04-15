package com.ivanjunckes.book.external;

import com.ivanjunckes.book.Book;
import com.ivanjunckes.common.ApiResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("external-books")
public class ExternalBookResource {

    @RestClient
    @Inject
    private IceAndFireBookClient client;

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResult<Book> getExternalBooks(@QueryParam("name") final String name) {
        Response response = client.getBooks(name);
        ApiResult<Book> apiResult = new ApiResult<>();
        if (response.getStatus() == 200) {
            List<IceAndFireBook> iceAndFireBooks = response.readEntity(new GenericType<List<IceAndFireBook>>() {});

            if(iceAndFireBooks != null){
                List<Book> externalBooks =
                        iceAndFireBooks.stream()
                                .map(b -> new Book(b))
                                .collect(Collectors.toList());
                apiResult.setData(externalBooks);
            }
            apiResult.setStatus("success");
        }
        apiResult.setStatusCode(response.getStatus());

        return apiResult;
    }
}
