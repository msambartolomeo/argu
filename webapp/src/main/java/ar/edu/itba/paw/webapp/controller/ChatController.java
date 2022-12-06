package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ChatService;
import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.webapp.dto.ChatDto;
import ar.edu.itba.paw.webapp.dto.ListDto;
import ar.edu.itba.paw.webapp.form.ChatForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/debates/{debateId}/chats")
public class ChatController {

    @PathParam("debateId")
    private long debateId;

    @Context
    private UriInfo uriInfo;

    @Autowired
    private ChatService chatService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getChats(
            @QueryParam("page") @DefaultValue("0") final int page,
            @Valid @Max(value = 30, message = "Page size exceeded") @QueryParam("size") @DefaultValue("15") final int size
    ) {
        final List<ChatDto> chatList = chatService.getDebateChat(debateId, page, size)
                .stream().map(c -> ChatDto.fromChat(uriInfo, c)).collect(Collectors.toList());

        if (chatList.isEmpty()) {
            return Response.noContent().build();
        }
        final int totalPages = chatService.getDebateChatPageCount(debateId, size);

        final ListDto<ChatDto> list = ListDto.from(chatList, totalPages, page, uriInfo);

        return Response.ok(new GenericEntity<ListDto<ChatDto>>(list) {}).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createChat(@Valid @NotNull final ChatForm form) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Chat chat = chatService.create(auth.getName(), debateId, form.getMessage());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(chat.getChatId())).build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMessageFromChat(@PathParam("id") final long id) {
        final Optional<ChatDto> chat = chatService.getChatById(id)
                .map(c -> ChatDto.fromChat(uriInfo, c));

        if (chat.isPresent()) {
            return Response.ok(chat.get()).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
