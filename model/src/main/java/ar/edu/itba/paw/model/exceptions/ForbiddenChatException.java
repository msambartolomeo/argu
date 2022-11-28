package ar.edu.itba.paw.model.exceptions;

public class ForbiddenChatException extends Exception403 {
    public ForbiddenChatException() {
        super("Forbidden Chat", "error.forbidden.chat");
    }
}
