package ar.edu.itba.paw.model.exceptions;

public abstract class StatusCodeException extends RuntimeException {
    private final int statusCode;

    public StatusCodeException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessageCode() { return "error.internal.server.error"; }
}
