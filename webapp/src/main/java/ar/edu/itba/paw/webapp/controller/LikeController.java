package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.LikeService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ForbiddenLikeException;
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

@Path("/debates/{debateId}/arguments/{argumentId}/likes")
@Component
public class LikeController {
    @PathParam("argumentId")
    private long argumentId;

    @Autowired
    private LikeService likeService;

    @POST
    public Response like(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenLikeException();
        }

        likeService.likeArgument(argumentId, username);

        return Response.created(null).build();
    }

    @DELETE
    public Response unlike(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenLikeException();
        }

        if (likeService.unlikeArgument(argumentId, username)) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    public Response getLike(
            @Valid @NotNull(message = "user param must be included") @QueryParam("user") final String url
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenLikeException();
        }

        if (likeService.isLiked(argumentId, username)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
