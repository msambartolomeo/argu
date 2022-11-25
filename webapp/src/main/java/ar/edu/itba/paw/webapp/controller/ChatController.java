package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ChatService;
import ar.edu.itba.paw.webapp.dto.ChatDto;
import ar.edu.itba.paw.webapp.dto.ListDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
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
            @QueryParam("page") @DefaultValue("0") int page,
            @Valid @Max(value = 30, message = "Page size exceeded") @QueryParam("size") @DefaultValue("15") int size
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
}
