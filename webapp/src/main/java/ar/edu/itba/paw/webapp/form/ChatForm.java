package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

public class ChatForm {

    @NotEmpty
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
