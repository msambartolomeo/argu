package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.DebateCategory;

import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<Debate> getDebateById(long id);
    Debate create(String name, String description, DebateCategory category);
    Debate create(String name, String description, byte[] image, DebateCategory category);
    List<Debate> getSubscribedDebatesByUsername(long userid, int page);
    int getSubscribedDebatesByUsernameCount(long userid);
    List<Debate> get(int page, String search);
    int getCount(String search);
    List<Debate> getMostSubscribed();
    List<Debate> getFromCategory(DebateCategory category, int page);
    int getFromCategoryCount(DebateCategory category);
}
