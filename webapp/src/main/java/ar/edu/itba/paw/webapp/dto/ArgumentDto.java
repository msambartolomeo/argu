package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import org.springframework.context.MessageSource;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Locale;

public class ArgumentDto {

    private String content;
    private String createdDate;
    private String status;
    private int likes;
    private boolean likedByUser;

    private URI self;
    private URI creator;
    private URI debate;
    private URI image;

    public static ArgumentDto fromArgument(final UriInfo uriInfo, final Argument argument, final MessageSource messageSource, final Locale locale) {
        ArgumentDto dto = new ArgumentDto();

        dto.content = argument.getContent();
        dto.createdDate = argument.getFormattedDate();
        dto.status = messageSource.getMessage("status." + argument.getStatus().getName(), null, locale);
        dto.likes = argument.getLikesCount();
        dto.likedByUser = argument.isLikedByUser();

        dto.self = uriInfo.getAbsolutePathBuilder().replacePath("arguments").path(String.valueOf(argument.getArgumentId())).build();

        Image image = argument.getImage();
        if (image != null) {
            dto.image = uriInfo.getAbsolutePathBuilder().replacePath("images").path(String.valueOf(image.getId())).build();
        }
        User creator = argument.getUser();
        if (creator != null && creator.getUsername() != null) {
            dto.creator = uriInfo.getAbsolutePathBuilder().replacePath("users").path(creator.getUrl()).build();
        }
        Debate debate = argument.getDebate();
        if (debate != null) {
            dto.debate = uriInfo.getAbsolutePathBuilder().replacePath("users").path(String.valueOf(debate.getDebateId())).build();
        }

        return dto;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
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

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }
}
