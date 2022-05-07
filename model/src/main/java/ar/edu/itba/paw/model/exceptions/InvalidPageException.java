package ar.edu.itba.paw.model.exceptions;

public class InvalidPageException extends Exception404 {
    @Override
    public String getMessageCode() {
        return "error.invalid.page";
    }
}
