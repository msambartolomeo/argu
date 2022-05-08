package ar.edu.itba.paw.model.exceptions;

public class Exception500 extends RuntimeException {

    public String getMessageCode() { return "internal.server.error"; }
}
