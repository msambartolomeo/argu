package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;

import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);
    Optional<PublicDebate> getPublicDebateById(long id);
    List<PublicDebate> getAll(int page);
    List<PublicDebate> getQuery(int page, String query);
    Debate create(String name, String description, Long creatorId, Long opponentId, Long imageId, DebateCategory category);
    List<PublicDebate> getSubscribedDebatesByUsername(long userid, int page);
    List<PublicDebate> getMostSubscribed();
    List<PublicDebate> getAllFromCategory(DebateCategory category, int page);
    int getAllcount();
    int getQueryCount(String query);
    int getSubscribedDebatesByUsernameCount(long userid);
    int getAllFromCategoryCount(DebateCategory category);

    void subscribeToDebate(long userid, long debateid);

    void unsubscribeToDebate(long userid, long debateid);

    boolean isUserSubscribed(long userid, long debateid);

    List<PublicDebate> getPublicDebatesGeneral(int page, int pageSize, String searchQuery, String category, String order, String open);

    int getPublicDebatesCount(String searchQuery, String category, String status);
}
