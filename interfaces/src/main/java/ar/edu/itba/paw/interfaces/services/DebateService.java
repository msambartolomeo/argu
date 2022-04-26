package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;

import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<Debate> getDebateById(long id);
    Optional<PublicDebate> getPublicDebateById(long id);
    Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category);
    List<PublicDebate> getSubscribedDebatesByUsername(long userid, int page);
    List<PublicDebate> get(int page, String search);
    List<PublicDebate> getMostSubscribed();
    List<PublicDebate> getFromCategory(DebateCategory category, int page);
    int getSubscribedDebatesByUsernameCount(long userid);
    int getCount(String search);
    int getFromCategoryCount(DebateCategory category);
}
