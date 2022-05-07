package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.NotExistingEmail;
import ar.edu.itba.paw.webapp.validators.NotExistingUsername;
import ar.edu.itba.paw.webapp.validators.PasswordEquality;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@PasswordEquality
public class RegisterForm {

    @Size(max = 64)
    @NotEmpty
    @NotExistingUsername
    private String username;

    @Size(max = 100)
    @Email
    @NotEmpty
    @NotExistingEmail
    private String email;

    @NotEmpty
    @Size(max = 100)
    private String password;

    @NotEmpty
    @Size(max = 100)
    private String passwordConfirmation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}


