package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateVote;

import java.util.Optional;

public interface VoteService {
    Vote addVote(long debateId, String username, DebateVote vote);
    Optional<Vote> getVote(long debateId, String username);
    void removeVote(long debateId, String username);
}
