package com.capgemini.sample.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(r -> r.mvcMatchers("/hello/**")
                                     .permitAll()
                                     .antMatchers("/h2-console/**")
                                     .permitAll()
                                     .anyRequest()
                                     .authenticated());
        http.formLogin();
        http.httpBasic();
        http.csrf()
            .disable(); // disable CSRF for H2 Console web UI (to avoid 403 Forbidden)
        http.headers()
            .frameOptions()
            .sameOrigin(); // enables <frame> in H2 Console web UI
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        final PasswordEncoder passwordEncoder = passwordEncoder();
        final String password = passwordEncoder.encode("password");
        auth.inMemoryAuthentication()
            .passwordEncoder(passwordEncoder)//
            .withUser("user")
            .password(password)
            .roles("USER")//
            .and()
            .withUser("admin")
            .password(password)
            .roles("USER", "ADMIN");
    }

    // @Bean
    PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder(13);
    }

}
