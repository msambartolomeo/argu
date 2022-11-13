package ar.edu.itba.paw.webapp.auth;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    // Authoriaztion: Basic (base64 username:password)
    // Authorization: Digest (username:hash(password))
    // Authorization: Bearer (hash(signed jsonwebtoken))
    // tambien hay refresh token para renovar el token porque tiene ttl mucho mas corto
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // browser request X-> 403 / 401
        // request X + Authoriaztion: Digest (username:hash(password))
        // server -> loguea al usuario + genera token + deja pasar request X + devuelve el token en el header
        // browser -> almacena los tokens en localstorage
        // request Z + Authorization: Bearer (token)

        // TODO implementar
    }
}