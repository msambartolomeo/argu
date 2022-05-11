package ar.edu.itba.paw.model.exceptions;

public abstract class Exception400 extends RuntimeException{

    public String getMessageCode() {
        return "BadRequest";
    }
}
