package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import java.util.List;

public class ListResponseBuilder {

    public static <T> Response buildResponse(List<T> list, int totalPages, int page, UriInfo uriInfo, long lastId) {
        return createResponse(list, totalPages, page, uriInfo)
                .link(uriInfo.getAbsolutePathBuilder().path(String.valueOf(lastId)).build(), "last_element")
                .build();
    }

    public static <T> Response buildResponse(List<T> list, int totalPages, int page, UriInfo uriInfo) {
        return createResponse(list, totalPages, page, uriInfo).build();
    }

    private static <T> Response.ResponseBuilder createResponse(List<T> list, int totalPages, int page, UriInfo uriInfo) {
        Response.ResponseBuilder builder = Response.ok(new GenericEntity<List<T>>(list) {})
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", 0).build(), "first")
                .link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", totalPages - 1).build(), "last");

        if (page > 0) {
            builder = builder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", page - 1).build(), "prev");
        }

        if (page < totalPages - 1) {
            builder = builder.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page", page + 1).build(), "next");
        }

        return builder;
    }
}
