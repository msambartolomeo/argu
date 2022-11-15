package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Debate;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class DebateDto {

    private long id;
    private String name;
    private String description;
    private boolean isCreatorFor;
    private String createdDate;
    private String category;
    private String status;

    private URI self;
    private URI image;
    private URI creator;
    private URI opponent;

    public static DebateDto fromDebate(final UriInfo uriInfo, final Debate debate) {
        final DebateDto dto = new DebateDto();

        dto.id = debate.getDebateId();
        dto.name = debate.getName();
        dto.description = debate.getDescription();
        dto.isCreatorFor = debate.getIsCreatorFor();
        dto.createdDate = debate.getFormattedDate();
        dto.category = debate.getCategory().getName();
        dto.status = debate.getStatus().getName();

        dto.self = uriInfo.getAbsolutePathBuilder().replacePath("debates").path(String.valueOf(debate.getDebateId())).build();

        if (debate.getImage() != null) {
            dto.image = uriInfo.getAbsolutePathBuilder().replacePath("images").path(String.valueOf(debate.getImage().getId())).build();
        }
        if (debate.getCreator() != null) {
            dto.creator = uriInfo.getAbsolutePathBuilder().replacePath("users").path(debate.getCreator().getUrl()).build();
        }
        if (debate.getOpponent() != null) {
            dto.opponent = uriInfo.getAbsolutePathBuilder().replacePath("users").path(debate.getOpponent().getUrl()).build();
        }

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCreatorFor() {
        return isCreatorFor;
    }

    public void setCreatorFor(boolean creatorFor) {
        isCreatorFor = creatorFor;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public URI getCreator() {
        return creator;
    }

    public void setCreator(URI creator) {
        this.creator = creator;
    }

    public URI getOpponent() {
        return opponent;
    }

    public void setOpponent(URI opponent) {
        this.opponent = opponent;
    }
}
