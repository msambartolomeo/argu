package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);

    Debate create(String name, String description, User creator, User opponent, Image image, DebateCategory category);

    List<Debate> getSubscribedDebatesByUserId(User user, int page);

    int getSubscribedDebatesByUserIdCount(long userid);

    List<PublicDebate> getPublicDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);

    List<Debate> getDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);

    int getDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date);

    int getPublicDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date);

    List<PublicDebate> getMyDebates(long userid, int page);

    List<Debate> getMyDebates(User user, int page);

    int getMyDebatesCount(User user);

    int getMyDebatesCount(long userid);
}
