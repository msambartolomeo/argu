package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import org.springframework.context.MessageSource;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Locale;

public class DebateDto {

    private long id;
    private String name;
    private String description;
    private boolean isCreatorFor;
    private String createdDate;
    private String category;
    private String status;
    private int subscriptions;
    private int votesFor;
    private int votesAgainst;

    private URI self;
    private URI image;
    private URI creator;
    private URI opponent;

    public static DebateDto fromDebate(final UriInfo uriInfo, final Debate debate, final MessageSource messageSource, final Locale locale) {
        final DebateDto dto = new DebateDto();

        dto.id = debate.getDebateId();
        dto.name = debate.getName();
        dto.description = debate.getDescription();
        dto.isCreatorFor = debate.getIsCreatorFor();
        dto.createdDate = debate.getFormattedDate();
        dto.category = messageSource.getMessage("category." + debate.getCategory().getName(), null, locale);
        dto.status = messageSource.getMessage("status." + debate.getStatus().getName(), null, locale);
        dto.subscriptions = debate.getSubscribedUsersCount();
        dto.votesFor = debate.getForCount();
        dto.votesAgainst = debate.getAgainstCount();

        dto.self = uriInfo.getAbsolutePathBuilder().replacePath("debates").path(String.valueOf(debate.getDebateId())).build();

        Image image = debate.getImage();
        if (image != null) {
            dto.image = uriInfo.getAbsolutePathBuilder().replacePath("images").path(String.valueOf(image.getId())).build();
        }
        User creator = debate.getCreator();
        if (creator != null && creator.getUsername() != null) {
            dto.creator = uriInfo.getAbsolutePathBuilder().replacePath("users").path(creator.getUrl()).build();
        }
        User opponent = debate.getOpponent();
        if (opponent != null && opponent.getUsername() != null) {
            dto.opponent = uriInfo.getAbsolutePathBuilder().replacePath("users").path(opponent.getUrl()).build();
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

    public int getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(int subscriptions) {
        this.subscriptions = subscriptions;
    }

    public int getVotesFor() {
        return votesFor;
    }

    public void setVotesFor(int votesFor) {
        this.votesFor = votesFor;
    }

    public int getVotesAgainst() {
        return votesAgainst;
    }

    public void setVotesAgainst(int votesAgainst) {
        this.votesAgainst = votesAgainst;
    }
}
