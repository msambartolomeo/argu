package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService us;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final static Pattern BYCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = us.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user by the name " + username));
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

        // Migrate users with un-hashed passwords
        //if(!BYCRYPT_PATTERN.matcher(user.getPassword()).matches())
            // TODO: Update user password

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
