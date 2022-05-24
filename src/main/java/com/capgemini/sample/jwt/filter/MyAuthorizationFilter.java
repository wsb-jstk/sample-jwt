package com.capgemini.sample.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.capgemini.sample.jwt.controller.AuthController;
import com.capgemini.sample.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyAuthorizationFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";
    private static final ObjectMapper MAPPER = JsonMapper.builder()
                                                         .build();
    /**
     * {@link AuthController Endpoint}, which refresh accessToken and refreshToken
     */
    private static final String AUTHENTICATE_ENDPOINT = "/auth";
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI()
                   .contains(AUTHENTICATE_ENDPOINT)) {
            // obtaining new accessToken and refreshToken based on refreshToken - do not verify provided refreshToken as it would be accessToken
            filterChain.doFilter(request, response);
            return;
        }
        final Optional<String> optionalEncodedToken = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                                                              .filter(h -> h.startsWith(BEARER))
                                                              .map(h -> h.substring(BEARER.length()));
        if (optionalEncodedToken.isPresent()) {
            final String token = optionalEncodedToken.get();
            try {
                UsernamePasswordAuthenticationToken authToken = jwtService.verifyToken(token);
                SecurityContextHolder.getContext()
                                     .setAuthentication(authToken);
            } catch (JWTVerificationException exception) {
                handleException(response, exception);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, JWTVerificationException exception) throws IOException {
        final Map<String, String> mapToken = Map.of("error", "Token is invalid", //
                "error_class", exception.getClass()
                                        .getName(), //
                "error_details", exception.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        MAPPER.writeValue(response.getOutputStream(), mapToken);
    }

}
