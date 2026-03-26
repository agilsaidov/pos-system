package com.app.pos.system.repo;

import com.app.pos.system.model.StoreAssignment;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.projection.CashierDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreAssignmentRepository extends JpaRepository<StoreAssignment, StoreAssignmentId> {

    @Query(value = """
        SELECT u.id userId,
              u.username username,
              u.email email,
              u.first_name firstName,
              u.last_name lastName,
              u.enabled enabled,
              u.created_at createdAt,
              s.id storeId,
              s.name storeName,
              r.name roleName
        FROM users u 
        JOIN store_assignments sa ON sa.user_id = u.id
        JOIN stores s ON s.id = sa.store_id
        JOIN user_roles ur ON ur.user_id = u.id    
        JOIN roles r ON r.id = ur.role_id
        WHERE sa.store_id = :storeId
        AND r.name = 'CASHIER'        
        """, nativeQuery = true)
    List<CashierDetailsProjection> findAllByStoreId(@Param("storeId") Long storeId);


    @Query(value = """
        SELECT u.id userId,
              u.username username,
              u.email email,
              u.first_name firstName,
              u.last_name lastName,
              u.enabled enabled,
              u.created_at createdAt,
              s.id storeId,
              s.name storeName,
              r.name roleName
        FROM users u 
        JOIN store_assignments sa ON sa.user_id = u.id
        JOIN stores s ON s.id = sa.store_id
        JOIN user_roles ur ON ur.user_id = u.id    
        JOIN roles r ON r.id = ur.role_id
        WHERE sa.store_id = :storeId
        AND u.id = :cashierId          
        AND r.name = 'CASHIER'        
        """, nativeQuery = true)
    CashierDetailsProjection getByUserIdAndStoreId(@Param("cashierId") Long cashierId, @Param("storeId") Long storeId);
}
