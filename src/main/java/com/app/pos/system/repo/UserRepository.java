package com.app.pos.system.repo;

import com.app.pos.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsUserByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(UUID uuid);
}
