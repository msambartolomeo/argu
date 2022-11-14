package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    private final static String ENCODING = "UTF-8";

    @Autowired
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{url}")
    public Response getUser(@PathParam("url") final String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, ENCODING);

        final Optional<UserDto> user = userService.getUserByUsername(username).map(u -> UserDto.fromUser(uriInfo, u));

        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(@Valid final RegisterForm form) throws UnsupportedEncodingException {
        final User user = userService.create(form.getUsername(), form.getPassword(), form.getEmail(), LocaleContextHolder.getLocale());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(URLEncoder.encode(user.getUsername(), ENCODING)).build()).build();
    }

    @DELETE
    @Path("/{url}")
    public Response deleteUser(@PathParam("url") final String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, ENCODING);

        userService.deleteUser(username);

        return Response.noContent().build();
    }

}
