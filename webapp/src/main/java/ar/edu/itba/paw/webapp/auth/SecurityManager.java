package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Component
public class SecurityManager {
    public boolean checkSameUser(Authentication auth, String userURL) throws UnsupportedEncodingException {
        final String username = URLDecoder.decode(userURL, User.ENCODING);

        return username.equals(auth.getName());
    }
}
