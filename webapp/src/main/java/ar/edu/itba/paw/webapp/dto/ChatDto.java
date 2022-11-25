package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ChatDto {

    private String message;
    private String createdDate;

    private URI self;
    private URI creator;
    private URI debate;

    public static ChatDto fromChat(final UriInfo uriInfo, final Chat chat) {
        final ChatDto dto = new ChatDto();

        dto.message = chat.getMessage();
        dto.createdDate = chat.getFormattedDate();

        dto.self = uriInfo.getAbsolutePathBuilder().replacePath("chats").path(String.valueOf(chat.getChatId())).build();

        User creator = chat.getUser();
        if (creator != null && creator.getUsername() != null) {
            dto.creator = uriInfo.getAbsolutePathBuilder().replacePath("users").path(creator.getUrl()).build();
        }
        Debate debate = chat.getDebate();
        if (debate != null) {
            dto.debate = uriInfo.getAbsolutePathBuilder().replacePath("debates").path(String.valueOf(debate.getDebateId())).build();
        }

        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getCreator() {
        return creator;
    }

    public void setCreator(URI creator) {
        this.creator = creator;
    }

    public URI getDebate() {
        return debate;
    }

    public void setDebate(URI debate) {
        this.debate = debate;
    }
}
