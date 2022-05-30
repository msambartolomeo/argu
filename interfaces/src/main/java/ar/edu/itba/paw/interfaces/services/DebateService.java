package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<Debate> getDebateById(long debateId);
    Debate create(String name, String description, String creatorUsername, String opponentUsername, byte[] image, DebateCategory category);
    List<Debate> get(int page, String search, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);
    int getPages(String search, DebateCategory category, DebateStatus status, LocalDate date);
    List<Debate> getMostSubscribed();
    List<Debate> getProfileDebates(String list, long userid, int page);
    int getProfileDebatesPageCount(String list, long userid);
    void startConclusion(long id, String username);
    void deleteDebate(long id, String username);
}