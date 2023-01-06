package ar.edu.itba.paw.model.exceptions;

public class ChatNotFoundException extends Exception404 {

    public ChatNotFoundException() {
        super("Chat message not found.", "error.not-found.chat");
    }
}
