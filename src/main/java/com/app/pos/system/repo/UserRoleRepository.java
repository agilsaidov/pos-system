package com.app.pos.system.repo;

import com.app.pos.system.model.UserRole;
import com.app.pos.system.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
