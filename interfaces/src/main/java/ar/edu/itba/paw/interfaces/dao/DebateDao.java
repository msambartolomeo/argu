package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);

    Debate create(String name, String description, User creator, User opponent, Image image, DebateCategory category);
    List<Debate> getSubscribedDebatesByUserId(long userid, int page);
    int getSubscribedDebatesByUserIdCount(long userid);

    @Deprecated
    void subscribeToDebate(long userid, long debateid);
    @Deprecated
    void unsubscribeToDebate(long userid, long debateid);
    @Deprecated
    boolean isUserSubscribed(long userid, long debateid);

    List<PublicDebate> getPublicDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);

    int getPublicDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date);

    List<PublicDebate> getMyDebates(long userid, int page);

    int getMyDebatesCount(long userid);

    @Deprecated // TODO: Delete deprecated methods
    void addVote(long debateId, long userId, DebateVote vote);
    @Deprecated
    void removeVote(long debateId, long userId);
    @Deprecated
    Boolean hasUserVoted(long debateId, long userId);
    @Deprecated
    DebateVote getUserVote(long debateid, long userid);
    @Deprecated
    void changeDebateStatus(long id, DebateStatus status);
}
