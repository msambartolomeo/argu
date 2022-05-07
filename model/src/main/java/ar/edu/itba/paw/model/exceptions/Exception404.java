package ar.edu.itba.paw.model.exceptions;

public abstract class Exception404 extends RuntimeException {

    public String getMessageCode() {
        return "NotFound";
    }

}
