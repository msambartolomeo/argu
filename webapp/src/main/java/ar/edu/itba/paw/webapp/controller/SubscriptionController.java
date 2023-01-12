package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.SubscribedService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.SubscriptionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @POST
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response subscribe(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        subscribedService.subscribeToDebate(username, debateId);

        return Response.created(null).build();
    }

    @DELETE
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response unsubscribe(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        if (subscribedService.unsubscribeToDebate(username, debateId)) {
            return Response.noContent().build();
        }

        throw new SubscriptionNotFoundException();
    }

    @GET
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response getSubscription(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        if (subscribedService.isUserSubscribed(username, debateId)) {
            return Response.ok().build();
        }

        throw new SubscriptionNotFoundException();
    }
}
