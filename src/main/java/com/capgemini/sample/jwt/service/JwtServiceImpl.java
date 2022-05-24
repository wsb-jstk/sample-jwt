package com.capgemini.sample.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String ISSUER = "issuer";
    private static final String ROLES = "roles";
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final UserDetailsService userDetailsService;

    public JwtServiceImpl(@Value("${jwt.secret}") final String jwtSecret, UserDetailsService userDetailsService) {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
        this.verifier = JWT.require(this.algorithm)
                           .build();
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String createAccessToken(org.springframework.security.core.userdetails.UserDetails userDetails) {
        return JWT.create()
                  .withSubject(userDetails.getUsername())
                  .withExpiresAt(getDateAfterMinutes(10))
                  .withIssuer(ISSUER)
                  .withClaim(ROLES, getRoles(userDetails))
                  .sign(this.algorithm);
    }

    @Override
    public String createRefreshToken(org.springframework.security.core.userdetails.UserDetails userDetails) {
        return JWT.create()
                  .withSubject(userDetails.getUsername())
                  .withExpiresAt(getDateAfterMinutes(30))
                  // .withNotBefore(getDateAfterMinutes(9))
                  .withIssuer(ISSUER)
                  .sign(this.algorithm);
    }

    @Override
    public UsernamePasswordAuthenticationToken verifyToken(String token) {
        final DecodedJWT decoded = verifier.verify(token);
        final String userName = decoded.getSubject();
        final List<String> roles = decoded.getClaim(ROLES)
                                          .asList(String.class);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                                                        .map(SimpleGrantedAuthority::new)
                                                        .toList();
        return new UsernamePasswordAuthenticationToken(userName, null, authorities);
    }

    @Override
    public UserDetails verifyRefreshToken(String token) {
        final DecodedJWT decoded = verifier.verify(token);
        final String userName = decoded.getSubject();
        return userDetailsService.loadUserByUsername(userName);
    }

    private List<String> getRoles(UserDetails userDetails) {
        return userDetails.getAuthorities()
                          .stream()
                          .map(GrantedAuthority::getAuthority)
                          .toList();
    }

    private Date getDateAfterMinutes(int minutes) {
        return new Date(Instant.now()
                               .plus(minutes, ChronoUnit.MINUTES)
                               .toEpochMilli());
    }

}
