package com.capgemini.sample.jwt;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class JwtApplication {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(JwtApplication.class).web(WebApplicationType.SERVLET)
                                                          .run(args);
    }

}
