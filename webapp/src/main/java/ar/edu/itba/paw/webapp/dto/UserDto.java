package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {

    private String username;
    private String createdDate;
    private int points;

    private URI self;
    private URI image;
    private URI debates;
    private URI subscribedDebates;

    public static UserDto fromUser(final UriInfo uriInfo, final User user) {
        final UserDto dto = new UserDto();

        dto.username = user.getUsername();
        dto.createdDate = user.getFormattedDate();
        dto.points = user.getPoints();

        final UriBuilder userBuilder = uriInfo.getBaseUriBuilder().path("users").path(user.getUrl());
        dto.self = userBuilder.build();
        dto.image = userBuilder.path("image").build();

        final UriBuilder userDebatesBuilder = uriInfo.getBaseUriBuilder().path("debates").queryParam("userUrl", user.getUrl());
        dto.debates = userDebatesBuilder.build();
        dto.subscribedDebates = userDebatesBuilder.queryParam("subscribed", true).build();
        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public URI getDebates() {
        return debates;
    }

    public void setDebates(URI debates) {
        this.debates = debates;
    }

    public URI getSubscribedDebates() {
        return subscribedDebates;
    }

    public void setSubscribedDebates(URI subscribedDebates) {
        this.subscribedDebates = subscribedDebates;
    }
}
