package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateVote;

import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<Debate> getDebateById(long id);
    Optional<PublicDebate> getPublicDebateById(long id);
    Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category);
    List<PublicDebate> get(int page, String search, String category, String order, String status, String date);
    List<PublicDebate> getMostSubscribed();
    int getPages(String search, String category, String status, String date);

    void subscribeToDebate(String username, long debateid);

    void unsubscribeToDebate(String username, long debateid);

    boolean isUserSubscribed(String username, long debateid);
    List<PublicDebate> getProfileDebates(String list, long userid, int page);
    int getProfileDebatesPageCount(String list, long userid);
    void addVote(long debateId, String username, DebateVote vote);
    void removeVote(long debateId, String username);
    String getUserVote(long debateid, String username);
    void startConclusion(long id, String username);
    void closeDebate(long id);
}
