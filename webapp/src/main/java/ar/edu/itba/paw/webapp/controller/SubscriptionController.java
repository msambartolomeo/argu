package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.SubscribedService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Path("/debates/{debateId}/subscriptions")
@Component
public class SubscriptionController {
    @PathParam("debateId")
    private long debateId;

    @Autowired
    private SubscribedService subscribedService;

    // TODO: ask query string for username instead of body or not sending at all
    // FIXME: Add auth guards
    @POST
    public Response subscribe(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);
        subscribedService.subscribeToDebate(username, debateId);

        return Response.created(null).build();
    }

    // FIXME: Add auth guards
    @DELETE
    public Response unsubscribe(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        if (subscribedService.unsubscribeToDebate(username, debateId)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    public Response getSubscription(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        if (subscribedService.isUserSubscribed(username, debateId)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
