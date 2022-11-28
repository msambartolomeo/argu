package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateVote;

public class VoteDto {

    private DebateVote vote;

    public static VoteDto fromVote(Vote vote) {
        final VoteDto dto = new VoteDto();

        dto.vote = vote.getVote();

        return dto;
    }


    public DebateVote getVote() {
        return vote;
    }

    public void setVote(DebateVote vote) {
        this.vote = vote;
    }
}
