package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class ListResponseBuilder {

    public static Response buildResponse(GenericEntity<?> list, int totalPages, int page, UriInfo uriInfo, long lastId) {
        return createResponse(list, totalPages, page, uriInfo)
                .link(uriInfo.getAbsolutePathBuilder().path(String.valueOf(lastId)).build(), "last_element")
                .build();
    }

    public static Response buildResponse(GenericEntity<?> list, int totalPages, int page, UriInfo uriInfo) {
        return createResponse(list, totalPages, page, uriInfo).build();
    }

    private static Response.ResponseBuilder createResponse(GenericEntity<?> list, int totalPages, int page, UriInfo uriInfo) {
        Response.ResponseBuilder builder = Response.ok(list)
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
