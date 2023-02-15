package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.LikeService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.LikeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Path("/debates/{debateId}/arguments/{argumentId}/likes")
@Component
public class LikeController {
    @PathParam("argumentId")
    private long argumentId;

    @Autowired
    private LikeService likeService;

    @POST
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response like(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        likeService.likeArgument(argumentId, username);

        return Response.created(null).build();
    }

    @DELETE
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response unlike(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        if (likeService.unlikeArgument(argumentId, username)) {
            return Response.noContent().build();
        }

        throw new LikeNotFoundException();
    }

    @GET
    @PreAuthorize("@securityManager.checkSameUser(authentication, #url)")
    public Response getLike(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        if (likeService.isLiked(argumentId, username)) {
            return Response.ok().build();
        }
        throw new LikeNotFoundException();
    }

}
