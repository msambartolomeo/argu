package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.webapp.dto.DebateDto;
import ar.edu.itba.paw.webapp.dto.ListDto;
import ar.edu.itba.paw.webapp.patches.DebatePatch;
import ar.edu.itba.paw.webapp.patches.PATCH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Response getDebates(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("search") String search,
            @QueryParam("category") String category,
            @QueryParam("order") String order,
            @QueryParam("status") String status,
            @QueryParam("date") String date
    ) {
        final String auxOrder = order;
        if (order != null && Arrays.stream(DebateOrder.values()).noneMatch((o) -> o.getName().equals(auxOrder)))
            order = null;
        if (status != null && !status.equals("open") && !status.equals("closed")) status = null;
        if (category != null && Arrays.stream(DebateCategory.values()).noneMatch((c) -> c.getName().equals(category))) {
            return Response.noContent().build();
        }
        if (date != null && !date.matches("\\d{2}-\\d{2}-\\d{4}")) date = null;

        final DebateCategory finalCategory = category == null ? null : DebateCategory.valueOf(category.toUpperCase());
        final DebateOrder finalOrder = order == null ? null : DebateOrder.valueOf(order.toUpperCase());
        final DebateStatus finalStatus = status == null ? null : DebateStatus.valueOf(status.toUpperCase());
        final LocalDate finalDate = date == null ? null : LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        final List<DebateDto> debateList = debateService.get(page, search, finalCategory, finalOrder, finalStatus, finalDate)
                .stream().map(d -> DebateDto.fromDebate(uriInfo, d)).collect(Collectors.toList());

        if (debateList.isEmpty()) {
            return Response.noContent().build();
        }
        int totalPages = debateService.getPages(search, finalCategory, finalStatus, finalDate);

        ListDto<DebateDto> list = ListDto.from(debateList, totalPages, page, uriInfo);
        return Response.ok(new GenericEntity<ListDto<DebateDto>>(list) {}).build();
    }

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

    // FIXME: Fix when Forms pr is merged
//    @POST
//    @Consumes({MediaType.MULTIPART_FORM_DATA})
//    public Response createDebate(@Valid CreateDebateForm form) {
//
//        final Debate debate = debateService.create(form.getTitle(),
//                form.getDescription(),
//                auth.getName(),
//                form.getIsCreatorFor(),
//                form.getOpponentUsername(),
//                form.getImage(),
//                form.getCategory());
//
//        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(debate.getDebateId())).build()).build();
//    }

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
