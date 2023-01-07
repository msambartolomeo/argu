package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {

    private String username;
    private String email;
    private String createdDate;
    private int points;

    private URI self;
    private URI image;

    public static UserDto fromUser(final UriInfo uriInfo, final User user) {
        final UserDto dto = new UserDto();

        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.createdDate = user.getFormattedDate();
        dto.points = user.getPoints();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(user.getUrl()).build();

        if (user.getImage() != null) {
            dto.image = uriInfo.getBaseUriBuilder().path("images").path(String.valueOf(user.getImage().getId())).build();
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
