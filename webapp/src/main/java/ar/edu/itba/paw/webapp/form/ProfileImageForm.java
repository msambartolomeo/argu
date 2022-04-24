package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

public class ProfileImageForm {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
