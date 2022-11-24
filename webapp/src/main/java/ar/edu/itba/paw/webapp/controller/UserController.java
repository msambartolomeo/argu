package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import ar.edu.itba.paw.webapp.validators.Image;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

@Path("users")
@Component
public class UserController {
    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

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
    public Response createUser(@Valid final RegisterForm form) {
        final User user = userService.create(form.getUsername(), form.getPassword(), form.getEmail(), LocaleContextHolder.getLocale());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUrl()).build()).build();
    }

    @DELETE
    @Path("/{url}")
    public Response deleteUser(@PathParam("url") final String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        userService.deleteUser(username);

        return Response.noContent().build();
    }

    // TODO: Add route in auth config
    @PUT
    @Path("/{url}/image")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response updateImage(
            @PathParam("url") String url,
            @FormDataParam("image") InputStream imageInput,
            @Valid @NotNull @Image @FormDataParam("image") FormDataBodyPart imageDetails
    ) throws IOException {
        final String username = URLDecoder.decode(url, User.ENCODING);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!username.equals(auth.getName())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Response.Status status = ImageUtils.checkError(imageDetails);
        if (status != null) {
            return Response.status(status).build();
        }
        byte[] image = ImageUtils.getImage(imageInput);

        User user = userService.updateImage(username, image);

        return Response.created(uriInfo.getAbsolutePathBuilder().replacePath("images")
                .path(String.valueOf(user.getImage().getId())).build()).build();
    }

    @DELETE
    @Path("/{url}/image")
    public Response removeImage(@PathParam("url") String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!username.equals(auth.getName())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        userService.deleteImage(username);

        return Response.noContent().build();
    }
}
