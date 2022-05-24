package com.capgemini.sample.jwt.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String createAccessToken(org.springframework.security.core.userdetails.UserDetails userDetails);

    String createRefreshToken(org.springframework.security.core.userdetails.UserDetails userDetails);

    /**
     * @throws JWTVerificationException on
     *         verification error
     */
    UsernamePasswordAuthenticationToken verifyToken(String token);

    /**
     * @throws JWTVerificationException on
     *         verification error
     */
    UserDetails verifyRefreshToken(String token);

}
