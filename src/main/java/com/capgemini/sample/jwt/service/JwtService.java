package com.capgemini.sample.jwt.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface JwtService {

    String createAccessToken(org.springframework.security.core.userdetails.User user);

    UsernamePasswordAuthenticationToken verifyToken(String token);

}
