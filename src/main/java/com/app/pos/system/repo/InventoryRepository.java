package com.app.pos.system.repo;

import com.app.pos.system.model.Inventory;
import com.app.pos.system.model.InventoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

}
