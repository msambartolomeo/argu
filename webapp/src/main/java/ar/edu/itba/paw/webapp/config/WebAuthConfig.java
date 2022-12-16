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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
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
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().headers().cacheControl().disable()
            .and().authorizeRequests()
                // users
                .antMatchers(HttpMethod.DELETE,"/api/users/{url}", "/api/users/{url}/image").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/users/{url}/image").authenticated()
                .antMatchers(HttpMethod.POST, "/api/users").anonymous()
                // debate
                .antMatchers(HttpMethod.DELETE, "/api/debates/{\\d+}").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.POST, "/api/debates").hasAuthority("MODERATOR")
                .antMatchers(HttpMethod.PATCH, "/api/debates/{\\d+}").authenticated()
                // argument and chat
                .antMatchers(HttpMethod.DELETE, "/api/debates/{\\d+}/arguments/{\\d+}").authenticated()
                .antMatchers(HttpMethod.POST, "/api/debates/{\\d+}/arguments", "/api/debates/{\\d+}/chats").authenticated()
                // likes, subs and votes
                .antMatchers("/api/debates/{debateId}/arguments/{argumentId}/likes", "/api/debates/{debateId}/subscriptions", "/api/debates/{debateId}/votes").authenticated()
                // general
                .antMatchers(HttpMethod.GET, "/api/users/{url}", "/api/debates/**").permitAll()
            .and().exceptionHandling()
                .accessDeniedHandler((request, response, ex) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN))
                .authenticationEntryPoint((request, response, ex) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
            .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring() // Ignore static resources
                .antMatchers("/")
                .antMatchers("/*.js")
                .antMatchers("/*.css")
                .antMatchers("/favicon.ico")
                .antMatchers("/manifest.json");;
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin("http://localhost:3000");
        return cors;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}