package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Argument;

import java.util.List;
import java.util.Optional;

public interface ArgumentService {
    Argument create(String username, long debateId, String content, byte[] image);
    int getArgumentByDebatePageCount(long debateId, int size);
    Optional<Argument> getArgumentById(long argumentId);
    List<Argument> getArgumentsByDebate(long debateId, String username, int page, int size);
    Optional<Argument> getLastArgument(long debateId);
    void deleteArgument(long argumentId, String username);
}
