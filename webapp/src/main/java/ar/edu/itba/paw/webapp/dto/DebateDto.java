package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateStatus;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DebateDto {

    private long id;
    private String name;
    private String description;
    private boolean isCreatorFor;
    private String createdDate;
    private String dateToClose;
    private String category;
    private String status;
    private int subscriptionsCount;
    private int votesFor;
    private int votesAgainst;
    private String creatorName;
    private String opponentName;

    private URI self;
    private URI image;
    private URI creator;
    private URI opponent;
    private URI arguments;
    private URI chats;
    private URI recommendations;
    private URI sameCategory;
    private URI sameStatus;
    private URI afterSameDate;
    private URI vote;
    private URI subscription;

    public static DebateDto fromDebate(final UriInfo uriInfo, final Debate debate, final MessageSource messageSource, final Locale locale) {
        final DebateDto dto = new DebateDto();

        dto.id = debate.getDebateId();
        dto.name = debate.getName();
        dto.description = debate.getDescription();
        dto.isCreatorFor = debate.getIsCreatorFor();
        dto.createdDate = debate.getFormattedDate();
        dto.dateToClose = debate.getFormattedDateToClose();
        dto.category = messageSource.getMessage("category." + debate.getCategory().getName(), null, locale);
        dto.status = messageSource.getMessage("status." + debate.getStatus().getName(), null, locale);
        dto.subscriptionsCount = debate.getSubscribedUsersCount();
        dto.votesFor = debate.getForCount();
        dto.votesAgainst = debate.getAgainstCount();

        final String id = String.valueOf(debate.getDebateId());

        dto.self = uriInfo.getBaseUriBuilder().path("debates").path(id).build();

        Image image = debate.getImage();
        if (image != null) {
            dto.image = uriInfo.getBaseUriBuilder().path("images").path(id).build();
        }
        User creator = debate.getCreator();
        if (creator != null && creator.getUsername() != null) {
            dto.creatorName = creator.getUsername();
            dto.creator = uriInfo.getBaseUriBuilder().path("users").path(creator.getUrl()).build();
        }
        User opponent = debate.getOpponent();
        if (opponent != null && opponent.getUsername() != null) {
            dto.opponentName = opponent.getUsername();
            dto.opponent = uriInfo.getBaseUriBuilder().path("users").path(opponent.getUrl()).build();
        }

        dto.arguments = uriInfo.getBaseUriBuilder().path("debates").path(id).path("arguments").build();
        dto.chats = uriInfo.getBaseUriBuilder().path("debates").path(id).path("chats").build();

        dto.recommendations = uriInfo.getBaseUriBuilder().path("debates").queryParam("recommendToDebate", String.valueOf(debate.getDebateId())).build();
        dto.sameCategory = uriInfo.getBaseUriBuilder().path("debates").queryParam("category", debate.getCategory().getName()).build();
        DebateStatus status = debate.getStatus();
        if (status == DebateStatus.CLOSING || status == DebateStatus.VOTING) {
            status = DebateStatus.OPEN;
        }
        dto.sameStatus = uriInfo.getBaseUriBuilder().path("debates").queryParam("status", status.getName()).build();
        dto.afterSameDate = uriInfo.getBaseUriBuilder().path("debates").queryParam("date", debate.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).build();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            try {
                dto.vote = uriInfo.getBaseUriBuilder().path("debates").path(id).path("votes")
                        .queryParam("user", URLEncoder.encode(auth.getName(), User.ENCODING)).build();
                dto.subscription = uriInfo.getBaseUriBuilder().path("debates").path(id).path("subscriptions")
                        .queryParam("user", URLEncoder.encode(auth.getName(), User.ENCODING)).build();
            } catch (UnsupportedEncodingException e) {
                // NOTE: Encoding is valid, should not happen
                throw new IllegalStateException("Invalid encoding", e);
            }
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

    public URI getArguments() {
        return arguments;
    }

    public void setArguments(URI arguments) {
        this.arguments = arguments;
    }

    public int getSubscriptionsCount() {
        return subscriptionsCount;
    }

    public void setSubscriptionsCount(int subscriptionsCount) {
        this.subscriptionsCount = subscriptionsCount;
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

    public URI getChats() {
        return chats;
    }

    public void setChats(URI chats) {
        this.chats = chats;
    }

    public URI getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(URI recommendations) {
        this.recommendations = recommendations;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public URI getSameCategory() {
        return sameCategory;
    }

    public void setSameCategory(URI sameCategory) {
        this.sameCategory = sameCategory;
    }

    public URI getSameStatus() {
        return sameStatus;
    }

    public void setSameStatus(URI sameStatus) {
        this.sameStatus = sameStatus;
    }

    public URI getAfterSameDate() {
        return afterSameDate;
    }

    public void setAfterSameDate(URI afterSameDate) {
        this.afterSameDate = afterSameDate;
    }

    public URI getVote() {
        return vote;
    }

    public void setVote(URI vote) {
        this.vote = vote;
    }

    public URI getSubscription() {
        return subscription;
    }

    public void setSubscription(URI subscription) {
        this.subscription = subscription;
    }

    public String getDateToClose() {
        return dateToClose;
    }

    public void setDateToClose(String dateToClose) {
        this.dateToClose = dateToClose;
    }
}
