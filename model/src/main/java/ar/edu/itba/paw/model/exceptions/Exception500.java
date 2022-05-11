package ar.edu.itba.paw.model.exceptions;

public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }

    public String getMessageCode() { return "error.internal.server.error"; }
}
