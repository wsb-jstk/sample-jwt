package com.capgemini.sample.jwt.filter;

import com.capgemini.sample.jwt.controller.AuthController;
import com.capgemini.sample.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Could be theoretically done as one of the endpoint for {@link AuthController}
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = JsonMapper.builder()
                                                        .build();
    private final JwtService jwtService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws
            IOException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        final String accessToken = this.jwtService.createAccessToken(user);
        final String refreshToken = this.jwtService.createRefreshToken(user);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final Map<String, String> mapToken = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        objectMapper.writeValue(response.getOutputStream(), mapToken);
    }

}
