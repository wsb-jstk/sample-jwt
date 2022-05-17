package com.capgemini.sample.jwt.service;

import com.capgemini.sample.jwt.domain.AppRole;
import com.capgemini.sample.jwt.domain.AppUser;
import com.capgemini.sample.jwt.repository.RoleRepository;
import com.capgemini.sample.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public AppUser saveUser(final AppUser user) {
        return this.userRepository.save(user);
    }

    @Override
    public AppRole saveRole(final AppRole role) {
        return this.roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(final String userName, final String roleName) {
        final AppUser user = this.userRepository.findByName(userName);
        Objects.requireNonNull(user, "User not found");
        final AppRole role = this.roleRepository.findByName(roleName);
        Objects.requireNonNull(role, "Role not found");
        user.getRoles()
            .add(role);
    }

    @Override
    public AppUser getUser(final String name) {
        return this.userRepository.findByName(name);
    }

    @Override
    public List<AppUser> getUsers() {
        return this.userRepository.findAll();
    }

}
