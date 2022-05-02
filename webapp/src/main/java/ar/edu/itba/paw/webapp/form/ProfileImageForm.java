package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.Image;
import ar.edu.itba.paw.webapp.validators.ImageSize;
import org.springframework.web.multipart.MultipartFile;

public class ProfileImageForm {

    private String fileName;
    public MultipartFile getFile() {
        return file;
    }

    @Image
    @ImageSize
    private MultipartFile file;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
