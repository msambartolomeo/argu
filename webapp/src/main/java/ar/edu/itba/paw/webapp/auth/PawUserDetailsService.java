package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService us;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = us.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user by the name " + username));
        final Set<GrantedAuthority> authorities = new HashSet<>();
        switch (user.getRole()) {
            case USER:
                authorities.add(new SimpleGrantedAuthority("USER"));
                break;
            case MODERATOR:
                authorities.add(new SimpleGrantedAuthority("MODERATOR"));
                break;
        }

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
