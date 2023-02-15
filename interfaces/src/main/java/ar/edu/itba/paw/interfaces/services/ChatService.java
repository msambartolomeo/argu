package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Chat create(String username, long debateId, String message);
    List<Chat> getDebateChat(long debateId, int page, int size);
    int getDebateChatPageCount(long debateId, int size);
    Optional<Chat> getChatById(long id);
}
