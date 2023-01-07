package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Component
public class SecurityManager {
    @Autowired
    private DebateService debateService;

    public boolean checkSameUser(Authentication auth, String userURL) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(userURL, User.ENCODING);

        return username.equals(auth.getName());
    }

    public boolean checkDebateCreator(Authentication auth, long debateId) {
        final Optional<Debate> debate = debateService.getDebateById(debateId);

        return debate.filter(d -> d.getCreator().getUsername().equals(auth.getName())).isPresent();
    }

    public boolean checkDebateParticipant(Authentication auth, long debateId) {
        final Optional<Debate> debate = debateService.getDebateById(debateId);

        return debate.filter(d -> d.getCreator().getUsername().equals(auth.getName())
                || d.getOpponent().getUsername().equals(auth.getName())).isPresent();
    }
}
