package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<Debate> getDebateById(long debateId);
    Debate create(String name, String description, String creatorUsername, boolean isCreatorFor, String opponentUsername, byte[] image,
                  DebateCategory category);
    List<Debate> get(int page, int size, String search, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);
    int getPages(int size, String search, DebateCategory category, DebateStatus status, LocalDate date);
    List<Debate> getUserDebates(String username, int page, int size, boolean subscribed);
    int getUserDebatesPageCount(String username, int size, boolean subscribed);
    void startConclusion(long id, String username);
    void deleteDebate(long id, String username);
    void closeVotes();
    List<Debate> getRecommendedDebates(long debateid);

    List<Debate> getRecommendedDebates(long debateid, String username);
}