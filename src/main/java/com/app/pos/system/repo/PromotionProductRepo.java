package com.app.pos.system.repo;

import com.app.pos.system.model.PromotionProduct;
import com.app.pos.system.model.PromotionProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionProductRepo extends JpaRepository<PromotionProduct, PromotionProductId> {


    @Query(value = """
        SELECT pp.* FROM promotion_products pp
        JOIN promotions p ON pp.promotion_id = p.id
        WHERE pp.product_id = :productId
        AND p.active = true
        AND pp.active=true
        AND p.starts_at <= :now
        AND p.ends_at >= :now
        LIMIT 1
        """, nativeQuery = true)
    Optional<PromotionProduct> findActivePromotionForProduct(
            @Param("productId") Long productId,
            @Param("now") OffsetDateTime now);



    @Query(value = """
        SELECT pp.* FROM promotion_products pp
        JOIN promotions p ON pp.promotion_id = p.id
        WHERE pp.product_id = :productId AND pp.active=true
        """, nativeQuery = true)
    Page<PromotionProduct> findAllByProductId(@Param("productId") Long productId, Pageable pageable);


    @Query(value = """
        SELECT pp.* FROM promotion_products pp
        JOIN products p ON pp.product_id = p.id
        WHERE pp.promotion_id = :promotionId
        """, nativeQuery = true)
    List<PromotionProduct> findAllByPromotionId(@Param("promotionId") Long promotionId);


    @Query(value = """
            SELECT EXISTS(
                            SELECT p.active FROM promotions as p
                            JOIN promotion_products as pp 
                            ON pp.promotion_id = p.id
                            WHERE pp.product_id=:productId 
                            AND p.active=true        
                            AND pp.active=true            
                        );
            """, nativeQuery = true)
    boolean existsActivePromotionByProductId(@Param("productId") Long productId);


    @Query(value = """
    SELECT pp.product_id
    FROM promotion_products pp
    WHERE pp.promotion_id = :promotionId
      AND EXISTS (
          SELECT 1
          FROM promotion_products other
          JOIN promotions pr ON other.promotion_id = pr.id
          WHERE other.product_id = pp.product_id
            AND pr.active = true
            AND other.active = true
            AND pp.active = true
            AND pr.id <> :promotionId
      )
    """, nativeQuery = true)
    Long getConflictingProductId(@Param("promotionId") Long promotionId);


    @Query(value = """
            SELECT EXISTS(
            SELECT 1
            FROM promotion_products as pp
            JOIN promotions as pr
            ON pp.promotion_id = pr.id
            WHERE pp.product_id=:productId
                AND pp.active = true
                AND pr.active = true
    );
    """, nativeQuery = true)
    boolean existsActivePromotionProduct(@Param("productId") Long productId);
}
