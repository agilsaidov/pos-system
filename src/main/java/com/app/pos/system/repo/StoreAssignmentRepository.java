package com.app.pos.system.repo;

import com.app.pos.system.model.StoreAssignment;
import com.app.pos.system.model.StoreAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreAssignmentRepository extends JpaRepository<StoreAssignment, StoreAssignmentId> {
}
