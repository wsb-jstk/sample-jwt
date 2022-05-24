package com.capgemini.sample.jwt.controller;

import com.capgemini.sample.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

import static com.capgemini.sample.jwt.filter.MyAuthorizationFilter.BEARER;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    /**
     * We can autowi re {@link HttpServletRequest} and get value from the header.
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

    /**
     * Performs verification and refreshing tokens based on required header `Authorization`.
     * The idea is, that the header should contain refreshToken but current implementation does not prevent user to use the accessToken (as long as it is still
     * valid)
     */
    @GetMapping("/refresh-token-with-header")
    public ResponseEntity<?> authorizeWithRefreshToken(HttpServletRequest request) {
        final Optional<String> optionalRefreshToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                                                              .filter(h -> h.startsWith(BEARER))
                                                              .map(h -> h.substring(BEARER.length()));
        if (optionalRefreshToken.isEmpty()) {
            throw new RuntimeException("Missing Authorization header with refresh token");
        }
        final String token = optionalRefreshToken.get();
        final UserDetails userDetails = jwtService.verifyRefreshToken(token);
        final String accessToken = jwtService.createAccessToken(userDetails);
        final String refreshToken = jwtService.createRefreshToken(userDetails);
        final Map<String, String> mapToken = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        return ResponseEntity.ok(mapToken);
    }

}
