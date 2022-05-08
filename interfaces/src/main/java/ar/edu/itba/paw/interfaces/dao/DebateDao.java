package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateVote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);
    Optional<PublicDebate> getPublicDebateById(long id);
    Debate create(String name, String description, Long creatorId, Long opponentId, Long imageId, DebateCategory category);
    List<PublicDebate> getSubscribedDebatesByUsername(long userid, int page);
    int getSubscribedDebatesByUsernameCount(long userid);

    void subscribeToDebate(long userid, long debateid);

    void unsubscribeToDebate(long userid, long debateid);

    boolean isUserSubscribed(long userid, long debateid);

    List<PublicDebate> getPublicDebatesGeneral(int page, int pageSize, String searchQuery, String category, String order, String open, String date);

    int getPublicDebatesCount(String searchQuery, String category, String status, String date);

    List<PublicDebate> getMyDebates(long userid, int page);

    int getMyDebatesCount(long userid);

    void addVote(long debateId, long userId, DebateVote vote);

    void removeVote(long debateId, long userId);

    Boolean hasUserVoted(long debateId, long userId);

    DebateVote getUserVote(long debateid, long userid);

    int getForVotesCount(long debateid);

    int getAgainstVotesCount(long debateid);
}
