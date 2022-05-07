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
    List<PublicDebate> get(String page, String search, String category, String order, String status, String date);
    List<PublicDebate> getMostSubscribed();
    int getPages(String search, String category, String status, String date);

    void subscribeToDebate(long userid, long debateid);

    void unsubscribeToDebate(long userid, long debateid);

    boolean isUserSubscribed(long userid, long debateid);


    List<PublicDebate> getProfileDebates(String list, long userid, int page);

    int getProfileDebatesPageCount(String list, long userid);

    void addVote(long debateId, String username, DebateVote vote);

    void removeVote(long debateId, String username);
}
