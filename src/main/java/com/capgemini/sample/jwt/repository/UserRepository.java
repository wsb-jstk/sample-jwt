package com.capgemini.sample.jwt.repository;

import com.capgemini.sample.jwt.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByName(String name);

}
