package com.app.pos.system.repo;

import com.app.pos.system.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StockMovementRepository extends JpaRepository<StockMovement, Long>,
                                                JpaSpecificationExecutor<StockMovement> {

}
