package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;

import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getDebateById(long id);
    List<Debate> getAll(int page);
    Debate create(String name, String description, Long imageId);
    List<Debate> getSubscribedDebatesByUsername(long userid, int page);
}
