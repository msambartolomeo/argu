package ar.edu.itba.paw.model.exceptions;

public abstract class StatusCodeException extends RuntimeException {
    private final int statusCode;

    private final String messageCode;

    public StatusCodeException(String message, int statusCode, String messageCode) {
        super(message);
        this.statusCode = statusCode;
        this.messageCode = messageCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
