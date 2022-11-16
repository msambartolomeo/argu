package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationContextProvider implements ContextResolver<Authentication> {

    @Override
    public Authentication getContext(Class<?> type) {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}