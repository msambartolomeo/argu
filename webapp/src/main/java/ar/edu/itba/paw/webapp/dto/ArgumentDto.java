package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Locale;

public class ArgumentDto {

    private String content;
    private String createdDate;
    private String status;
    private int likes;
    private boolean likedByUser;
    private boolean deleted;
    private String creatorName;

    private URI self;
    private URI creator;
    private URI debate;
    private URI image;
    private URI like;

    public static ArgumentDto fromArgument(final UriInfo uriInfo, final Argument argument, final MessageSource messageSource, final Locale locale) {
        ArgumentDto dto = new ArgumentDto();

        dto.deleted = argument.getDeleted();
        if (dto.deleted) {
            dto.content = "";
            dto.likedByUser = false;
        } else {
            dto.content = argument.getContent();
            dto.likedByUser = argument.isLikedByUser();
        }
        dto.likes = argument.getLikesCount();
        dto.createdDate = argument.getFormattedDate();
        dto.status = messageSource.getMessage("status." + argument.getStatus().getName(), null, locale);

        final String id = String.valueOf(argument.getArgumentId());
        final String debateId = String.valueOf(argument.getDebate().getDebateId());
        dto.debate = uriInfo.getBaseUriBuilder().path("debates").path(debateId).build();
        dto.self = uriInfo.getBaseUriBuilder().path("debates").path(debateId).path("arguments").path(id).build();

        Image image = argument.getImage();
        if (image != null) {
            dto.image = uriInfo.getBaseUriBuilder().path("images").path(String.valueOf(image.getId())).build();
        }
        User creator = argument.getUser();
        if (creator != null && creator.getUsername() != null) {
            dto.creator = uriInfo.getBaseUriBuilder().path("users").path(creator.getUrl()).build();
            dto.creatorName = creator.getUsername();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            try {
                dto.like = uriInfo.getBaseUriBuilder().path("debates").path(debateId).path("arguments").path(id)
                        .queryParam("user", URLEncoder.encode(auth.getName(), User.ENCODING)).build();
            } catch (UnsupportedEncodingException e) {
                // NOTE: Encoding is valid, should not happen
                throw new IllegalStateException("Invalid encoding", e);
            }
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public URI getLike() {
        return like;
    }

    public void setLike(URI like) {
        this.like = like;
    }
}
