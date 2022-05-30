package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.VerifyPassword;
import org.hibernate.validator.constraints.NotEmpty;

public class ConfirmationForm {

    @NotEmpty
    @VerifyPassword
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
