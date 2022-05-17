package com.capgemini.sample.jwt.controller;

import com.capgemini.sample.jwt.dto.Greetings;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    /**
     * @see <a href="http://localhost:8080/jwt/hello/world">Open</a>
     */
    @GetMapping("/world")
    public Greetings helloWorld() {
        return new Greetings("Hello world!");
    }

    /**
     * @see <a href="http://localhost:8080/jwt/hello/user">Open</a>
     */
    @GetMapping("/user")
    @Secured("ROLE_USER")
    Greetings helloUser() {
        return new Greetings("Hello user!");
    }

    /**
     * @see <a href="http://localhost:8080/jwt/hello/admin">Open</a>
     */
    @GetMapping("/admin")
    @Secured("ROLE_ADMIN")
    Greetings helloAdmin() {
        return new Greetings("Hello admin!");
    }

}
