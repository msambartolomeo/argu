package ar.edu.itba.paw.model.exceptions;

import ar.edu.itba.paw.model.enums.DebateCategory;

public class DebateOponentException extends RuntimeException {

    private final String oponent;
    private final String debateTitle;
    private final String debateDescription;
    private final DebateCategory debateCategory;

    public DebateOponentException(String oponent, String debateTitle, String debateDescription, DebateCategory debateCategory) {
        this.oponent = oponent;
        this.debateTitle = debateTitle;
        this.debateDescription = debateDescription;
        this.debateCategory = debateCategory;
    }

    public String getOponent() {
        return oponent;
    }

    public String getDebateTitle() {
        return debateTitle;
    }

    public String getDebateDescription() {
        return debateDescription;
    }

    public DebateCategory getDebateCategory() {
        return debateCategory;
    }
}
