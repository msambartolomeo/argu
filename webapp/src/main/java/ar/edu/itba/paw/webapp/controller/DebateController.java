package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.webapp.dto.DebateDto;
import ar.edu.itba.paw.webapp.form.CreateDebateForm;
import ar.edu.itba.paw.webapp.patches.DebatePatch;
import ar.edu.itba.paw.webapp.patches.PATCH;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import ar.edu.itba.paw.webapp.utils.ListResponseBuilder;
import ar.edu.itba.paw.webapp.validators.ExistingUser;
import ar.edu.itba.paw.webapp.validators.Image;
import ar.edu.itba.paw.webapp.validators.UserNotSelf;
import ar.edu.itba.paw.webapp.validators.ValidCategory;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/debates")
@Component
public class DebateController {

    @Autowired
    private DebateService debateService;
    @Autowired
    private MessageSource messageSource;

    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getDebates(
            @QueryParam("page") @DefaultValue("0") int page,
            @Valid @Max(value = 10, message = "Page size exceeded") @QueryParam("size") @DefaultValue("5") final int size,
            @QueryParam("search") final String search,
            @Valid @ValidCategory @QueryParam("category") final String category,
            @QueryParam("order") String order,
            @QueryParam("status") String status,
            @QueryParam("date") String date,
            @QueryParam("recommendToDebate") final Long recommendToDebate,
            @QueryParam("userUrl") final String userUrl,
            @QueryParam("subscribed") final String subscribed
    ) throws UnsupportedEncodingException {
        final String auxOrder = order;
        if (order != null && Arrays.stream(DebateOrder.values()).noneMatch((o) -> o.getName().equals(auxOrder)))
            order = null;
        if (status != null && !status.equals("open") && !status.equals("closed")) status = null;
        if (date != null && !date.matches("\\d{2}-\\d{2}-\\d{4}")) date = null;

        final DebateCategory finalCategory = category == null ? null : DebateCategory.valueOf(category.toUpperCase());
        final DebateOrder finalOrder = order == null ? null : DebateOrder.valueOf(order.toUpperCase());
        final DebateStatus finalStatus = status == null ? null : DebateStatus.valueOf(status.toUpperCase());
        final LocalDate finalDate = date == null ? null : LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        final List<DebateDto> debateList;
        final int totalPages;
        if (recommendToDebate != null) {
            final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = null;
            if (auth != null && auth.getPrincipal() != null && !auth.getPrincipal().equals("anonymousUser")) {
                username = auth.getName();
            }

            debateList = debateService.getRecommendedDebates(recommendToDebate, username)
                    .stream().map(d -> DebateDto.fromDebate(uriInfo, d, messageSource, request.getLocale())).collect(Collectors.toList());

            if (debateList.isEmpty()) {
                return Response.noContent().build();
            }

            totalPages = 1;
            page = 0;
        } else if (userUrl != null) {
            final String username = URLDecoder.decode(userUrl, User.ENCODING);
            debateList = debateService.getUserDebates(username, page, size, subscribed != null).stream()
                    .map(d -> DebateDto.fromDebate(uriInfo, d, messageSource, request.getLocale())).collect(Collectors.toList());

            if (debateList.isEmpty()) {
                return Response.noContent().build();
            }

            totalPages = debateService.getUserDebatesPageCount(username, size, subscribed != null);
        } else {
            debateList = debateService.get(page, size, search, finalCategory, finalOrder, finalStatus, finalDate)
                    .stream().map(d -> DebateDto.fromDebate(uriInfo, d, messageSource, request.getLocale())).collect(Collectors.toList());

            if (debateList.isEmpty()) {
                return Response.noContent().build();
            }

            totalPages = debateService.getPages(size, search, finalCategory, finalStatus, finalDate);
        }

        return ListResponseBuilder.buildResponse(new GenericEntity<List<DebateDto>>(debateList) {}, totalPages, page, uriInfo);
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response getDebate(@PathParam("id") final long id) {
        final Optional<DebateDto> debate = debateService.getDebateById(id)
                .map(d -> DebateDto.fromDebate(uriInfo, d, messageSource, request.getLocale()));

        if (debate.isPresent()) {
            return Response.ok(debate.get()).build();
        }

        throw new DebateNotFoundException();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createDebate(@Valid @NotNull CreateDebateForm form) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final Debate debate = debateService.create(form.getTitle(),
                form.getDescription(),
                auth.getName(),
                form.getIsCreatorFor(),
                form.getOpponentUsername(),
                new byte[0],
                DebateCategory.valueOf(form.getCategory().toUpperCase()));

        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(debate.getDebateId())).build()).build();
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response createDebateWithImage(
            @FormDataParam("image") final InputStream imageInput,
            @Valid @Image @FormDataParam("image") final FormDataBodyPart imageDetails,
            @Valid @Size(max = 100) @NotEmpty @FormDataParam("title") final String title,
            @Valid @Size(max = 280) @NotEmpty @FormDataParam("description") final String description,
            @Valid @NotNull @ValidCategory @FormDataParam("category") final String category,
            @Valid @NotNull @FormDataParam("isCreatorFor") final Boolean isCreatorFor,
            @Valid @Size(max = 64) @NotEmpty @ExistingUser @UserNotSelf @FormDataParam("opponentUsername") final String opponentUsername
    ) throws IOException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final byte[] image = ImageUtils.getImage(imageDetails, imageInput);

        final Debate debate = debateService.create(title,
                description,
                auth.getName(),
                isCreatorFor,
                opponentUsername,
                image,
                DebateCategory.valueOf(category.toUpperCase()));

        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(debate.getDebateId())).build()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDebate(@PathParam("id") final long id) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        debateService.deleteDebate(id, auth.getName());

        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response patchDebate(@PathParam("id") final long id, @Valid @NotNull final DebatePatch patch) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (patch.isConclusion()) {
            debateService.startConclusion(id, auth.getName());
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
