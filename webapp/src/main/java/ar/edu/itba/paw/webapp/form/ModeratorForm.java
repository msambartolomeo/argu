package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

public class ModeratorForm {
    @NotEmpty
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
