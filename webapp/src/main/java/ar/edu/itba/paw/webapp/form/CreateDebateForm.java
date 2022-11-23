package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.webapp.validators.ExistingUser;
import ar.edu.itba.paw.webapp.validators.UserNotSelf;
import ar.edu.itba.paw.webapp.validators.ValidCategory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateDebateForm {

    @Size(max = 100)
    @NotEmpty
    private String title;

    @Size(max = 280)
    @NotEmpty
    private String description;

    @ValidCategory
    @NotNull
    private String category;

    @NotNull
    private boolean isCreatorFor;

    @Size(max = 64)
    @NotEmpty
    @ExistingUser
    @UserNotSelf
    private String opponentUsername;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public boolean getIsCreatorFor() {
        return isCreatorFor;
    }

    public void setIsCreatorFor(boolean creatorFor) {
        isCreatorFor = creatorFor;
    }
}
