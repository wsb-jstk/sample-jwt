package com.capgemini.sample.jwt;

import com.capgemini.sample.jwt.domain.AppRole;
import com.capgemini.sample.jwt.domain.AppUser;
import com.capgemini.sample.jwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class JwtApplication {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(JwtApplication.class).web(WebApplicationType.SERVLET)
                                                          .run(args);
    }

    /**
     * Create some users and roles in the DB
     */
    @Bean
    CommandLineRunner run(final UserService userService, final PasswordEncoder passwordEncoder) {
        return args -> {
            // set up roles
            userService.saveRole(new AppRole(null, "ROLE_USER"));
            userService.saveRole(new AppRole(null, "ROLE_ADMIN"));
            // set up users
            final String encodedPassword = passwordEncoder.encode("password");
            userService.saveUser(new AppUser(null, "user", encodedPassword, new ArrayList<>()));
            userService.saveUser(new AppUser(null, "admin", encodedPassword, new ArrayList<>()));
            // set up roles
            userService.addRoleToUser("user", "ROLE_USER");
            userService.addRoleToUser("admin", "ROLE_USER");
            userService.addRoleToUser("admin", "ROLE_ADMIN");
        };
    }

}
