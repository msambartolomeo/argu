package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<PublicDebate> getPublicDebateById(long id);
    Debate create(String name, String description, Long creatorId, Long opponentId, Long imageId, DebateCategory category);
    List<PublicDebate> getSubscribedDebatesByUserId(long userid, int page);
    int getSubscribedDebatesByUserIdCount(long userid);

    void subscribeToDebate(long userid, long debateid);

    void unsubscribeToDebate(long userid, long debateid);

    boolean isUserSubscribed(long userid, long debateid);

    List<PublicDebate> getPublicDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);

    int getPublicDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date);

    List<PublicDebate> getMyDebates(long userid, int page);

    int getMyDebatesCount(long userid);

    void addVote(long debateId, long userId, DebateVote vote);

    void removeVote(long debateId, long userId);

    Boolean hasUserVoted(long debateId, long userId);

    DebateVote getUserVote(long debateid, long userid);

    void changeDebateStatus(long id, DebateStatus status);
}
