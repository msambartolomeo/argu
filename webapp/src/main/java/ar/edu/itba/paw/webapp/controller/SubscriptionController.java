package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.SubscribedService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ForbiddenSubscriptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Path("/api/debates/{debateId}/subscriptions")
@Component
public class SubscriptionController {
    @PathParam("debateId")
    private long debateId;

    @Autowired
    private SubscribedService subscribedService;

    @POST
    public Response subscribe(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenSubscriptionException();
        }

        subscribedService.subscribeToDebate(username, debateId);

        return Response.created(null).build();
    }

    @DELETE
    public Response unsubscribe(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenSubscriptionException();
        }

        if (subscribedService.unsubscribeToDebate(username, debateId)) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response getSubscription(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenSubscriptionException();
        }

        if (subscribedService.isUserSubscribed(username, debateId)) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
