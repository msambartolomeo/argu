package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.webapp.validators.ExistingUser;
import ar.edu.itba.paw.webapp.validators.Image;
import ar.edu.itba.paw.webapp.validators.ImageSize;
import ar.edu.itba.paw.webapp.validators.UserNotSelf;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateDebateForm {

    @Size(max = 100)
    @NotEmpty
    private String title;

    @Size(max = 280)
    @NotEmpty
    private String description;

    @NotNull
    private DebateCategory category;

    @NotNull
    private boolean isCreatorFor;

    @Size(max = 64)
    @NotEmpty
    @ExistingUser
    @UserNotSelf
    private String opponentUsername;

    @ImageSize
    @Image
    private MultipartFile image;

    private String imageName;

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

    public DebateCategory getCategory() {
        return category;
    }

    public void setCategory(DebateCategory category) {
        this.category = category;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean getIsCreatorFor() {
        return isCreatorFor;
    }

    public void setIsCreatorFor(boolean creatorFor) {
        isCreatorFor = creatorFor;
    }
}
