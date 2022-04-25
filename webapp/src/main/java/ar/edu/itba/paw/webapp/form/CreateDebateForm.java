package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.Image;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

public class CreateDebateForm {

    @Size(max = 64)
    @NotEmpty
    private String title;

    @Size(max = 280)
    @NotEmpty
    private String description;

    @NotEmpty
    private int categoryId;

    @Image
    private MultipartFile image;

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
