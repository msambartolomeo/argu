package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);

    Debate create(String name, String description, User creator, boolean isCreatorFor, User opponent, Image image, DebateCategory category);

    List<Debate> getSubscribedDebatesByUser(long userId, int page, int size);

    int getSubscribedDebatesByUserCount(long userid);

    List<Debate> getDebatesDiscovery(int page, int pageSize, String searchQuery, DebateCategory category, DebateOrder order, DebateStatus status, LocalDate date);

    int getDebatesCount(String searchQuery, DebateCategory category, DebateStatus status, LocalDate date);

    List<Debate> getUserDebates(long userId, int page, int size);

    int getUserDebatesCount(long userid);

    List<Debate> getDebatesToClose();

    List<Debate> getRecommendedDebates(Debate debate);

    List<Debate> getRecommendedDebates(Debate debate, User user);
}
