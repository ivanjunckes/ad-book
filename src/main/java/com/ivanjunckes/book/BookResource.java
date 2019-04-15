package com.ivanjunckes.book;

import com.ivanjunckes.common.ApiResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
@Path("v1/books")
public class BookResource {

    @Inject
    private BookDao bookDao;

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response create(Book book) {
        try {
            bookDao.create(book);
            ApiResult<Book> apiResult = new ApiResult<>();
            apiResult.setStatusCode(Response.Status.CREATED.getStatusCode());
            apiResult.setData(Arrays.asList(book));
            return Response.status(Response.Status.CREATED).entity(apiResult).build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response read(@QueryParam("name") final String name, @QueryParam("country") final String country,
                         @QueryParam("publisher") final String publisher, @QueryParam("year") final Integer year) {
        try {
            ApiResult<Book> apiResult = new ApiResult<>();
            apiResult.setStatusCode(Response.Status.OK.getStatusCode());
            List<Book> books = bookDao.read(name, country, publisher, year);
            if (books != null) {
                apiResult.setData(books);
            }
            return Response.ok(apiResult).build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PATCH
    @Path("{id}")
    public Response patch(@PathParam("id") final Integer id, Book book) {
        try {
            Book databaseBook = bookDao.find(id);
            Book patchedBook = databaseBook.patch(book);
            bookDao.update(patchedBook);
            ApiResult<Book> apiResult = new ApiResult<>();
            apiResult.setStatusCode(Response.Status.OK.getStatusCode());
            apiResult.setData(Arrays.asList(patchedBook));
            apiResult.setMessage("The book " + patchedBook.getName() + " was updated successfully");
            return Response.ok(apiResult).build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("{id}")
    public Response show(@PathParam("id") final Integer id) {
        try {
            ApiResult<Book> apiResult = new ApiResult<>();

            Book book = bookDao.find(id);
            if (book != null) {
                apiResult.setStatusCode(Response.Status.OK.getStatusCode());
                apiResult.setData(Arrays.asList(book));
                return Response.ok(apiResult).build();
            } else {
                apiResult.setStatusCode(Response.Status.OK.getStatusCode());
                return Response.status(Response.Status.OK).entity(apiResult).build();
            }

        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") final Integer id) {
        try {
            bookDao.delete(id);
            ApiResult<Book> apiResult = new ApiResult<>();
            apiResult.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
            return Response.status(Response.Status.NO_CONTENT).entity(apiResult).build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST).build());
        }
    }
}
