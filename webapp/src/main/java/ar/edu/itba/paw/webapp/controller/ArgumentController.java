package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ArgumentService;
import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.exceptions.ArgumentNotFoundException;
import ar.edu.itba.paw.webapp.dto.ArgumentDto;
import ar.edu.itba.paw.webapp.form.ArgumentForm;
import ar.edu.itba.paw.webapp.form.ArgumentPatch;
import ar.edu.itba.paw.webapp.utils.PATCH;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import ar.edu.itba.paw.webapp.utils.ListResponseBuilder;
import ar.edu.itba.paw.webapp.validators.Image;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/debates/{debateId}/arguments")
@Component
public class ArgumentController {

    @PathParam("debateId")
    private long debateId;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ArgumentService argumentService;

    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getArguments(
            @QueryParam("page") @DefaultValue("0") final int page,
            @Valid @Max(value = 10, message = "Page size exceeded") @QueryParam("size") @DefaultValue("5") final int size
    ) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = null;
        if (auth != null && auth.getPrincipal() != null && !auth.getPrincipal().equals("anonymousUser")) {
            username = auth.getName();
        }

        final List<ArgumentDto> argumentList = argumentService.getArgumentsByDebate(debateId, username, page, size)
                .stream().map(a -> ArgumentDto.fromArgument(uriInfo, a, messageSource, request.getLocale())).collect(Collectors.toList());

        if (argumentList.isEmpty()) {
            return Response.noContent().build();
        }
        final int totalPages = argumentService.getArgumentByDebatePageCount(debateId, size);

        final Optional<Argument> lastArgument = argumentService.getLastArgument(debateId);

        final GenericEntity<List<ArgumentDto>> genericEntity = new GenericEntity<List<ArgumentDto>>(argumentList) {};
        if (lastArgument.isPresent()) {
            return ListResponseBuilder.buildResponse(genericEntity, totalPages, page, uriInfo, lastArgument.get().getArgumentId());
        }
        return ListResponseBuilder.buildResponse(genericEntity, totalPages, page, uriInfo);
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getArgument(@PathParam("id") final long id) {
        final Optional<ArgumentDto> argument = argumentService.getArgumentById(id)
                .map(a -> ArgumentDto.fromArgument(uriInfo, a, messageSource, request.getLocale()));

        if (argument.isPresent()) {
            return Response.ok(argument.get()).build();
        }

        throw new ArgumentNotFoundException();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @PreAuthorize("@securityManager.checkDebateParticipant(authentication, #debateId)")
    public Response createArgument(
            @Valid @NotNull(message = "body required") final ArgumentForm form,
            @PathParam("debateId") final long debateId
    ) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final Argument argument = argumentService.create(auth.getName(), debateId, form.getContent(), new byte[0]);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(argument.getArgumentId())).build()).build();
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @PreAuthorize("@securityManager.checkDebateParticipant(authentication, #debateId)")
    public Response createArgumentWithImage(
            @PathParam("debateId") final long debateId,
            @FormDataParam("image") final InputStream imageInput,
            @Valid @Image @FormDataParam("image") final FormDataBodyPart imageDetails,
            @Valid @NotEmpty @FormDataParam("content") final String content
    ) throws IOException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final byte[] image = ImageUtils.getImage(imageDetails, imageInput);

        final Argument argument = argumentService.create(auth.getName(), debateId, content, image);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(argument.getArgumentId())).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @PreAuthorize("@securityManager.checkArgumentCreator(authentication, #id)")
    public Response deleteArgument(@PathParam("id") final long id, @Valid @NotNull final ArgumentPatch patch) {
        if (patch.isDelete()) {
            argumentService.deleteArgument(id);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
