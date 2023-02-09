package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class ModeratorForm {
    @NotEmpty
    @Size(max = 2000)
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
