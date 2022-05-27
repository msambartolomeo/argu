package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<PublicDebate> getPublicDebateById(long id);
    Optional<Debate> getDebateById(long id);
    Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category);
    List<PublicDebate> get(int page, String search, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);
    int getPages(String search, DebateCategory category, DebateStatus status, LocalDate date);
    List<PublicDebate> getMostSubscribed();

    @Deprecated // TODO
    void subscribeToDebate(String username, long debateid);
    @Deprecated
    void unsubscribeToDebate(String username, long debateid);
    @Deprecated
    boolean isUserSubscribed(String username, long debateid);


    List<PublicDebate> getProfileDebates(String list, long userid, int page);
    int getProfileDebatesPageCount(String list, long userid);

    @Deprecated // TODO
    void addVote(long debateId, String username, DebateVote vote);
    @Deprecated
    void removeVote(long debateId, String username);
    @Deprecated
    String getUserVote(long debateid, String username);


    void startConclusion(long id, String username);
    void closeDebate(long id);
}