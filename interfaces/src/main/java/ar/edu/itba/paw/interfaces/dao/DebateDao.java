package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.DebateCategory;

import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);
    List<Debate> getAll(int page);
    int getAllcount();
    List<Debate> getQuery(int page, String query);
    int getQueryCount(String query);
    Debate create(String name, String description, Long imageId, DebateCategory category);
    List<Debate> getSubscribedDebatesByUsername(long userid, int page);
    int getSubscribedDebatesByUsernameCount(long userid);
    List<Debate> getMostSubscribed();
    List<Debate> getAllFromCategory(DebateCategory category, int page);
    int getAllFromCategoryCount(DebateCategory category);
}
