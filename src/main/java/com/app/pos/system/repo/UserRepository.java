package com.app.pos.system.repo;

import com.app.pos.system.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsUserByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE email = :input OR username = :input", nativeQuery = true)
    Optional<User> findByEmailOrUsername(@Param("input") String input);
}
