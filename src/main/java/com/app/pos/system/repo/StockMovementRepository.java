package com.app.pos.system.repo;

import com.app.pos.system.model.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>,
                                                JpaSpecificationExecutor<StockMovement> {

}
