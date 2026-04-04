package com.app.pos.system.repo;

import com.app.pos.system.model.PromotionProduct;
import com.app.pos.system.model.PromotionProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface PromotionProductRepo extends JpaRepository<PromotionProduct, PromotionProductId> {

    @Query(value = """
        SELECT pp.* FROM promotion_products pp
        JOIN promotions p ON pp.promotion_id = p.id
        WHERE pp.product_id = :productId
        AND p.active = true
        AND p.starts_at <= :now
        AND p.ends_at >= :now
        LIMIT 1
        """, nativeQuery = true)
    Optional<PromotionProduct> findActivePromotionForProduct(
            @Param("productId") Long productId,
            @Param("now") OffsetDateTime now);
}
