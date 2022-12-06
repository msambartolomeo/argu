package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface ChatDao {
    Chat create(User user, Debate debate, String message);
    List<Chat> getDebateChat(Debate debate, int page, int size);
    int getDebateChatsCount(long debateId);
    Optional<Chat> getChatById(long id);
}
