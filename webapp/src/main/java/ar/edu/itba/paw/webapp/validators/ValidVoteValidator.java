package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.model.enums.DebateVote;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidVoteValidator implements ConstraintValidator<ValidVote, String> {

    @Override
    public void initialize(ValidVote constraintAnnotation) {

    }

    @Override
    public boolean isValid(String vote, ConstraintValidatorContext context) {
        return vote == null || Arrays.stream(DebateVote.values()).anyMatch((v) -> v.getName().equals(vote.toLowerCase()));
    }
}
