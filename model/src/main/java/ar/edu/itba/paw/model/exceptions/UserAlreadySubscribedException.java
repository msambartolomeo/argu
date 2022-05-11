package ar.edu.itba.paw.model.exceptions;

public class UserAlreadySubscribedException extends Exception400{
    @Override
    public String getMessageCode() {
        return "error.already-subscribed";
    }
}
