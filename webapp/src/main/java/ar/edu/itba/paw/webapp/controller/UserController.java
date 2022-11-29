package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.DebateDto;
import ar.edu.itba.paw.webapp.dto.ListDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import ar.edu.itba.paw.webapp.validators.Image;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("users")
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
    public Response createUser(@Valid @NotNull final RegisterForm form) {
        final User user = userService.create(form.getUsername(), form.getPassword(), form.getEmail(), request.getLocale());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUrl()).build()).build();
    }

    @DELETE
    @Path("/{url}")
    public Response deleteUser(@PathParam("url") final String url) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        userService.deleteUser(username);

        return Response.noContent().build();
    }

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

    @Autowired
    private DebateService debateService;
    @Autowired
    private MessageSource messageSource;

    @GET
    @Path("/{url}/debates")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserDebates(
            @PathParam("url") final String url,
            @QueryParam("subscribed") final boolean subscribed,
            @QueryParam("page") @DefaultValue("0") final int page,
            @Valid @Max(value = 10, message = "Page size exceeded") @QueryParam("size") @DefaultValue("5") final int size
    ) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(url, User.ENCODING);

        final List<DebateDto> debateList = debateService.getUserDebates(username, page, size, subscribed).stream()
                .map(d -> DebateDto.fromDebate(uriInfo, d, messageSource, request.getLocale())).collect(Collectors.toList());

        if (debateList.isEmpty()) {
            return Response.noContent().build();
        }
        final int totalPages = debateService.getUserDebatesPageCount(username, size, subscribed);

        ListDto<DebateDto> list = ListDto.from(debateList, totalPages, page, uriInfo);
        return Response.ok(new GenericEntity<ListDto<DebateDto>>(list) {}).build();
    }
}
