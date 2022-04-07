package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Debate;

import java.util.List;
import java.util.Optional;

public interface DebateDao {
    Optional<Debate> getPostById(long id);
    List<Debate> getAll(int page, int pageSize);
    Debate create(String name, String description);
}
