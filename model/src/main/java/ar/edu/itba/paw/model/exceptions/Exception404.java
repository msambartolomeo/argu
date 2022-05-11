package ar.edu.itba.paw.model.exceptions;

public abstract class Exception404 extends RuntimeException {

    public Exception404(String message) {
        super(message);
    }
    public String getMessageCode() {
        return "NotFound";
    }

}
