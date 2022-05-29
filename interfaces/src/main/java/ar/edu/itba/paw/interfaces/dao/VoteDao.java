package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateVote;

import java.util.Optional;

public interface VoteDao {

    Vote addVote(User user, Debate debate, DebateVote vote);

    Optional<Vote> getVote(User user, Debate debate);

    void delete(Vote vote);
}
