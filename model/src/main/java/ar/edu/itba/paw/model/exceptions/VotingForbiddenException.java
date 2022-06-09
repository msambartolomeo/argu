package ar.edu.itba.paw.model.exceptions;

public class VotingForbiddenException extends Exception403{
    public VotingForbiddenException() {
        super("Voting has been disabled due to closed or deleted debate");
    }

    @Override
    public String getMessageCode() {
        return "error.forbidden.voting";
    }
}
