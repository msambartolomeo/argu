package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ForbiddenUserException;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import ar.edu.itba.paw.webapp.validators.Image;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Path("/api/users")
@Component
public class UserController {
    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;
    @Context
    private HttpServletRequest request;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{url}")
    public Response getUser(@PathParam("url") final String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Optional<UserDto> user = userService.getUserByUsername(username).map(u -> UserDto.fromUser(uriInfo, u));

        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(@Valid @NotNull(message = "body required") final RegisterForm form) {
        final User user = userService.create(form.getUsername(), form.getPassword(), form.getEmail(), request.getLocale());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUrl()).build()).build();
    }

    @DELETE
    @Path("/{url}")
    public Response deleteUser(@PathParam("url") final String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenUserException();
        }

        userService.deleteUser(username);

        return Response.noContent().build();
    }

    @PUT
    @Path("/{url}/image")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response updateImage(
            @PathParam("url") final String url,
            @FormDataParam("image") final InputStream imageInput,
            @Valid @NotNull @Image @FormDataParam("image") final FormDataBodyPart imageDetails
    ) throws IOException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenUserException();
        }

        final byte[] image = ImageUtils.getImage(imageDetails, imageInput);

        final User user = userService.updateImage(username, image);

        return Response.created(uriInfo.getAbsolutePathBuilder().replacePath("images")
                .path(String.valueOf(user.getImage().getId())).build()).build();
    }

    @DELETE
    @Path("/{url}/image")
    public Response removeImage(@PathParam("url") final String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenUserException();
        }

        if (userService.deleteImage(username)) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
