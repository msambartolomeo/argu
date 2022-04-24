package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Debate;

import java.util.List;
import java.util.Optional;

public interface DebateService {
    Optional<Debate> getDebateById(long id);
    Debate create(String name, String description);
    Debate create(String name, String description, byte[] image);
    List<Debate> getSubscribedDebatesByUsername(long userid, int page);
    List<Debate> get(int page, String search);
}
