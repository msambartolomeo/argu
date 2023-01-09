package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

public class CacheResponseBuilder {

    public static Response buildResponse(final long id, final Object entity, final Request request) {
        final EntityTag tag = new EntityTag(String.valueOf(id));

        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoTransform(true);
        cacheControl.setMustRevalidate(true);
        Response.ResponseBuilder response = request.evaluatePreconditions(tag);

        if (response == null)
            response = Response.ok(entity).tag(tag);

        return response.cacheControl(cacheControl).build();
    }
}
