package com.capgemini.sample.jwt.controller;

import com.capgemini.sample.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    /**
     * We can autowire {@link HttpServletRequest} and get value from the header.
     * For an example-sake we will pass the refresh token as query param ({@link RequestParam})
     */
    @GetMapping("/refresh-token")
    public ResponseEntity<?> authorizeWithRefreshToken(@RequestParam("token") final String token) {
        final UserDetails userDetails = jwtService.verifyRefreshToken(token);
        final String accessToken = jwtService.createAccessToken(userDetails);
        final String refreshToken = jwtService.createRefreshToken(userDetails);
        final Map<String, String> mapToken = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        return ResponseEntity.ok(mapToken);
    }

}
