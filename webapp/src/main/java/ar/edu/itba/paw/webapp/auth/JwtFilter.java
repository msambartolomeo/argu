package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    // JSON Web Tokens
    // Authoriaztion: Basic (base64 username:password)
    // Authorization: Digest (username:hash(password))
    // Authorization: Bearer (hash(signed jsonwebtoken))
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        boolean continueFilter = true;

        if (header != null && !header.isEmpty()) {
            if (header.startsWith("Basic ")) {
                continueFilter = basicAuthentication(header, request, response);
            } else if (header.startsWith("Bearer ")) {
                continueFilter = bearerAuthentication(header, request, response);
            } else {
                continueFilter = false;
            }
        }

        if (continueFilter) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean basicAuthentication(String header, HttpServletRequest request, HttpServletResponse response) {
        final String encodedCredentials = header.split(" ")[1];
        try {
            String[] credentials = new String(Base64.getDecoder().decode(encodedCredentials)).split(":");
            if (credentials.length != 2)
                return false;

            String username = credentials[0].trim();
            String password = credentials[1].trim();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, password);

            Authentication authenticate = authenticationManager.authenticate(authentication);

            UserDetails user = (UserDetails) authenticate.getPrincipal();
            User fullUser = userService.getUserByUsername(user.getUsername()).orElse(null);
            if (fullUser == null)
                return false;

            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.generateToken(fullUser));
            response.setHeader("X-Refresh", "Bearer " + jwtUtils.generateRefreshToken(fullUser));

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException | IllegalArgumentException ignored) {
            return false;
        }
        return true;
    }

    private boolean bearerAuthentication(String header, HttpServletRequest request, HttpServletResponse response) {
        final String token = header.split(" ")[1].trim();

        if (!jwtUtils.validate(token)) {
            return false;
        }

        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(jwtUtils.getUsernameFromToken(token));
        } catch (UsernameNotFoundException e) {
            user = null;
        }

        if (jwtUtils.isTokenRefresh(token) && user != null) {

            User fullUser = userService.getUserByUsername(user.getUsername()).orElse(null);
            if (fullUser == null)
                return false;

            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtils.generateToken(fullUser));
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, user == null ? Collections.emptyList() : user.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}