package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

public class ArgumentForm {
    @NotEmpty
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
