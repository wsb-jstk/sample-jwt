package com.capgemini.sample.jwt.service;

import com.capgemini.sample.jwt.domain.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final AppUser appUser = this.userService.getUser(username);
        if (appUser == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        final List<GrantedAuthority> roles = appUser.getRoles()
                                                    .stream()
                                                    .map(r -> new SimpleGrantedAuthority(r.getName()))
                                                    .collect(Collectors.toList());
        return new User(appUser.getName(), appUser.getPassword(), roles);
    }

}
