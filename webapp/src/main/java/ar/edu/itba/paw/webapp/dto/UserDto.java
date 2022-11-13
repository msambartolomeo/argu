package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class UserDto {

    private String username;
    private String email;
    private String createdDate;
    private URI self;
    private URI image;

    public static UserDto fromUser(final UriInfo uriInfo, final User user) {
        final UserDto dto = new UserDto();

        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.createdDate = user.getFormattedDate();

        try {
            dto.self = uriInfo.getAbsolutePathBuilder().replacePath("users").path(URLEncoder.encode(user.getUsername(), "UTF-8")).build();
        } catch (UnsupportedEncodingException e) {
            // NOTE: Should not happen
            throw new IllegalStateException("Unsupported encoding");
        }

        if (user.getImage() != null) {
            dto.image = uriInfo.getAbsolutePathBuilder().replacePath("images").path(String.valueOf(user.getImage().getId())).build();
        }

        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }
}
