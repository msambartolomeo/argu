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
    List<PublicDebate> get(String page, String search, String category, String order, String status, String date);
    List<PublicDebate> getMostSubscribed();

    int getSubscribedDebatesByUsernameCount(long userid);
    int getPages(String search, String category, String status, String date);

    void subscribeToDebate(long userid, long debateid);

    void unsubscribeToDebate(long userid, long debateid);

    boolean isUserSubscribed(long userid, long debateid);
}
