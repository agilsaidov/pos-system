package com.app.pos.system.repo;

import com.app.pos.system.model.Sale;
import com.app.pos.system.projection.DetailedCashierReportProjection;
import com.app.pos.system.projection.CashierReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query(value = """
                    SELECT u.id as cashierId,\s
                           u.full_name as cashierName,\s
                           COUNT(s.id) saleCount,\s
                           SUM(s.total) as revenue,\s
                           SUM(s.discount_total) as discountTotal,\s
                           SUM(s.tax_total) as taxTotal
                    FROM sales s
                    JOIN users u\s
                    ON s.cashier_id = u.id
                    WHERE s.store_id = :storeId
                    AND s.created_at >= :from
                    AND s.created_at <= :to
                    GROUP BY u.id, u.full_name""",
            nativeQuery = true)
    List<CashierReportProjection> getReports(@Param("storeId") Long storeId,
                                             @Param("from") OffsetDateTime from,
                                             @Param("to") OffsetDateTime to);


    @Query(value = """
                    SELECT u.id as cashierId,\s
                           u.full_name as cashierName,\s
                           COUNT(s.id) saleCount,\s
                           SUM(s.total) as revenue,\s
                           SUM(s.discount_total) as discountTotal,\s
                           SUM(s.tax_total) as taxTotal,
                           ROUND(AVG(s.total), 3) as averageSaleValue\s
                    FROM sales s \s
                    JOIN users u\s
                    ON s.cashier_id = u.id
                    WHERE s.cashier_id = :cashierId
                    AND s.store_id = :storeId
                    AND s.created_at >= :from
                    AND s.created_at <= :to
                    GROUP BY u.id, u.full_name;              
                    """,
            nativeQuery = true)
    DetailedCashierReportProjection getDetailedReport(@Param("cashierId") Long cashierId,
                                                      @Param("storeId") Long storeId,
                                                      @Param("from") OffsetDateTime from,
                                                      @Param("to") OffsetDateTime to);
}
