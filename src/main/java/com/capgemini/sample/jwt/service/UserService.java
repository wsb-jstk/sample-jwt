package com.capgemini.sample.jwt.service;

import com.capgemini.sample.jwt.domain.AppRole;
import com.capgemini.sample.jwt.domain.AppUser;

import java.util.List;

public interface UserService {

    AppUser saveUser(AppUser user);

    AppRole saveRole(AppRole role);

    void addRoleToUser(String userName, String roleName);

    AppUser getUser(String name);

    List<AppUser> getUsers();

}
