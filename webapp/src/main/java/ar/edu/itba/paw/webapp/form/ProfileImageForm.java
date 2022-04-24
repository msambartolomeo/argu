package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.Image;
import org.springframework.web.multipart.MultipartFile;

public class ProfileImageForm {

    @Image
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
