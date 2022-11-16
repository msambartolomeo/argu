package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.webapp.dto.DebateDto;
import ar.edu.itba.paw.webapp.form.CreateDebateForm;
import ar.edu.itba.paw.webapp.patches.DebatePatch;
import ar.edu.itba.paw.webapp.patches.PATCH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

@Path("debates")
@Component
public class DebateController {

    @Autowired
    private DebateService debateService;

    @Context
    private UriInfo uriInfo;
    @Context
    private Authentication auth;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response getDebate(@PathParam("id") final long id) {
        final Optional<DebateDto> debate = debateService.getDebateById(id).map(d -> DebateDto.fromDebate(uriInfo, d));

        if (debate.isPresent()) {
            return Response.ok(debate.get()).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response createDebate(@Valid CreateDebateForm form) {

        final Debate debate = debateService.create(form.getTitle(),
                form.getDescription(),
                auth.getName(),
                form.getIsCreatorFor(),
                form.getOpponentUsername(),
                form.getImage(),
                form.getCategory());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(debate.getDebateId())).build()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDebate(@PathParam("id") final long id) {
        debateService.deleteDebate(id, auth.getName());

        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response patchDebate(@PathParam("id") final long id, final DebatePatch patch) {
        if (patch.isConclusion()) {
            debateService.startConclusion(id, auth.getName());
        }

        return Response.noContent().build();
    }
}
