package com.capgemini.sample.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
@AllArgsConstructor
public class Greetings {

    private final String message;
    private final Object principal;

    public Greetings(final String message) {
        this.message = message;
        this.principal = SecurityContextHolder.getContext()
                                              .getAuthentication()
                                              .getPrincipal();
    }

}
