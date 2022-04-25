package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.DebateCategory;

import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<Debate> getDebateById(long id);
    Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category);
    List<Debate> getSubscribedDebatesByUsername(long userid, int page);
    List<Debate> get(int page, String search);
    List<Debate> getMostSubscribed();
    List<Debate> getFromCategory(DebateCategory category, int page);
}
