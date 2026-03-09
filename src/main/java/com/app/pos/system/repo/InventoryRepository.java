package com.app.pos.system.repo;

import com.app.pos.system.model.Inventory;
import com.app.pos.system.model.InventoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

    Page<Inventory> getInventoryByStoreStoreId(Long storeId, Pageable pageable);
}
