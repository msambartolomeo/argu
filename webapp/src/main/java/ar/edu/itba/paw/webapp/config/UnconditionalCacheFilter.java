package ar.edu.itba.paw.webapp.config;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnconditionalCacheFilter extends OncePerRequestFilter {
    private static final int MAX_TIME = 31536000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Cache-Control", "public, max-age=" + MAX_TIME + ", immutable");
        filterChain.doFilter(request, response);
    }
}
