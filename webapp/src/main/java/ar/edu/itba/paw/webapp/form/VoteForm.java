package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.ValidVote;

import javax.validation.constraints.NotNull;

public class VoteForm {
    @ValidVote
    @NotNull
    private String vote;

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
