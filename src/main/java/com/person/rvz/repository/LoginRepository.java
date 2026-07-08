package com.person.rvz.repository;

import com.person.rvz.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoginRepository extends JpaRepository<Login, UUID> {

    Optional<Login> findByUsername(String username);

    boolean existsByUsername(String username);

}
