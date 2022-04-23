package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;

import java.util.List;
import java.util.Optional;

public interface DebateService {

    Optional<Debate> getDebateById(long id);
    Debate create(String name, String description);
    List<Debate> getAll(int page);
    List<Debate> getSubscribedDebatesByUsername(long userid, int page);
}
