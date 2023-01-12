package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtFilter;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PawUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().headers().cacheControl().disable()
            .and().authorizeRequests()
                // users
                .antMatchers(HttpMethod.DELETE,"/users/{url}", "/users/{url}/image").access("@securityManager.checkSameUser(authentication, #url)")
                .antMatchers(HttpMethod.PUT, "/users/{url}/image").access("@securityManager.checkSameUser(authentication, #url)")
                .antMatchers(HttpMethod.POST, "/users").anonymous()
                // debate
                .antMatchers(HttpMethod.DELETE, "/debates/{\\d+}").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.POST, "/debates").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.PATCH, "/debates/{\\d+}").authenticated()
                // argument and chat
                .antMatchers(HttpMethod.DELETE, "/debates/{\\d+}/arguments/{\\d+}").authenticated()
                .antMatchers(HttpMethod.POST, "/debates/{\\d+}/arguments", "/debates/{\\d+}/chats").authenticated()
                // likes, subs and votes
                .antMatchers("/debates/{debateId}/arguments/{argumentId}/likes", "/debates/{debateId}/subscriptions", "/debates/{debateId}/votes").authenticated()
                // general
                .antMatchers(HttpMethod.GET, "/users/{url}", "/debates/**").permitAll()
            .and().exceptionHandling()
                .accessDeniedHandler((request, response, ex) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN))
                .authenticationEntryPoint((request, response, ex) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
            .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring() // Ignore static resources
                .antMatchers("/static/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        cors.setAllowedMethods(Collections.singletonList("*"));
        cors.setAllowedHeaders(Collections.singletonList("*"));
        cors.setExposedHeaders(Arrays.asList("Authorization", "X-Refresh"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}