package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.Image;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class PostForm {
    @NotEmpty
    private String content;

    @Image
    private MultipartFile file;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
