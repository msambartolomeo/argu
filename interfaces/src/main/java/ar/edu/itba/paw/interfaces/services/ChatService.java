package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Chat;

import java.util.List;

public interface ChatService {
    Chat create(String username, long debateId, String message);
    List<Chat> getDebateChat(long debateId, int page);
    int getDebateChatsCount(long debateId);
}
