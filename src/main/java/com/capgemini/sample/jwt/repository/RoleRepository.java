package com.capgemini.sample.jwt.repository;

import com.capgemini.sample.jwt.domain.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<AppRole, Long> {

    AppRole findByName(String name);

}
