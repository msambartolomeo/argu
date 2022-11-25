package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ArgumentService;
import ar.edu.itba.paw.webapp.dto.ArgumentDto;
import ar.edu.itba.paw.webapp.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
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
            @QueryParam("page") @DefaultValue("0") int page,
            @Valid @Max(value = 10, message = "Page size exceeded") @QueryParam("size") @DefaultValue("5") int size
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        if (auth != null && auth.getPrincipal() != null) {
            username = auth.getName();
        } else {
            username = null;
        }

        final List<ArgumentDto> argumentList = argumentService.getArgumentsByDebate(debateId, username, page, size)
                .stream().map(a -> ArgumentDto.fromArgument(uriInfo, a, messageSource, request.getLocale())).collect(Collectors.toList());

        if (argumentList.isEmpty()) {
            return Response.noContent().build();
        }
        int totalPages = argumentService.getArgumentByDebatePageCount(debateId, size);

        ListDto<ArgumentDto> list = ListDto.from(argumentList, totalPages, page, uriInfo);
        return Response.ok(new GenericEntity<ListDto<ArgumentDto>>(list) {}).build();
    }
}
