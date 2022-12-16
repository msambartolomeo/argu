package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final Key key;

    @Autowired
    public JwtUtils(Environment env) {
        this.key = Keys.hmacShaKeyFor(env.getProperty("jwt.secret").getBytes());
    }

    // TODO: Check expiration time
    private static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject).split(",")[0];
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(User user) {
        final Claims claims = Jwts.claims();
        claims.setSubject(user.getUrl());
        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        claims.put("points", user.getPoints());

        return Jwts.builder().setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
