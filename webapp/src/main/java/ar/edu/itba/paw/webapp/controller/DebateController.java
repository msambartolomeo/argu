package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.webapp.dto.DebateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

}
