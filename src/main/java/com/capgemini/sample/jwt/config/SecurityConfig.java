package com.capgemini.sample.jwt.config;

import com.capgemini.sample.jwt.filter.AuthenticationFilter;
import com.capgemini.sample.jwt.filter.MyAuthorizationFilter;
import com.capgemini.sample.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final MyAuthorizationFilter authorizationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(r -> r.mvcMatchers("/hello/**").permitAll()
                                     .antMatchers("/h2-console/**", "/actuator/**", "/error").permitAll()
                                     .antMatchers( "/auth/**").permitAll() // we do not need to add /login, since the auth is done on filter-level and not controller-level
                                     .antMatchers(HttpMethod.GET, "/api/users").permitAll()//
                                     .antMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole("USER")//
                                     .antMatchers(HttpMethod.POST, "/api/user/**", "/api/role/**").hasAnyRole("ADMIN")//
                                     .anyRequest().authenticated());
        // http.formLogin(); // adds UsernamePasswordAuthenticationFilter into the chain for POST /login
        http.httpBasic(); // adds BasicAuthenticationFilter into the security chain
        http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // IF_REQUIRED is a default one; needed if You want to use WWW and be able to login and access secured resources; use STATELESS if You will use only REST requests with Tokens
        http.csrf().disable(); // disable CSRF for H2 Console web UI (to avoid 403 Forbidden)
        http.headers().frameOptions().sameOrigin(); // enables <frame> in H2 Console web UI
        http.addFilter(new AuthenticationFilter(authenticationManagerBean(), this.jwtService));
        http.addFilterBefore(this.authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService)
            .passwordEncoder(passwordEncoder());

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder(13);
    }

}
