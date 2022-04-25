package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.DebateCategory;

import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);
    List<Debate> getAll(int page);

    List<Debate> getQuery(int page, String query);
    Debate create(String name, String description, Long creatorId, Long opponentId, Long imageId, DebateCategory category);
    List<Debate> getSubscribedDebatesByUsername(long userid, int page);
    List<Debate> getMostSubscribed();
    List<Debate> getAllFromCategory(DebateCategory category, int page);
}
