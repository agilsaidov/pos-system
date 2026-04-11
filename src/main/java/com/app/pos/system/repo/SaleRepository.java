package com.app.pos.system.repo;

import com.app.pos.system.model.Sale;
import com.app.pos.system.projection.DailyReportProjection;
import com.app.pos.system.projection.DetailedCashierReportProjection;
import com.app.pos.system.projection.CashierReportProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {
    @Query(value = """
                    SELECT u.id as cashierId,\s
                           u.first_name as firstName,\s
                           u.last_name as lastName,\s
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
                    GROUP BY u.id, u.first_name, u.last_name""",
            nativeQuery = true)
    List<CashierReportProjection> getReports(@Param("storeId") Long storeId,
                                             @Param("from") OffsetDateTime from,
                                             @Param("to") OffsetDateTime to);


    @Query(value = """
                    SELECT u.id as cashierId,\s
                           u.first_name as firstName,\s
                           u.last_name as lastName,\s                           COUNT(s.id) saleCount,\s
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
                    GROUP BY u.id, u.first_name, u.last_name;              
                    """,
            nativeQuery = true)
    DetailedCashierReportProjection getDetailedReport(@Param("cashierId") Long cashierId,
                                                      @Param("storeId") Long storeId,
                                                      @Param("from") OffsetDateTime from,
                                                      @Param("to") OffsetDateTime to);


    @Query(value = """
                   SELECT s.id as storeId,
                          s.name storeName, 
                          SUM(sa.total) as revenue,
                          SUM(sa.discount_total) as discountTotal,\s
                          SUM(sa.tax_total) as taxTotal,
                          COUNT(sa.id) as salesTotal
                   FROM stores as s 
                   JOIN sales sa           
                   ON s.id = sa.store_id  
                   WHERE sa.store_id = :storeId
                         AND DATE(sa.created_at) = DATE(:date)   
                   GROUP BY s.id, s.name;
               """,
            nativeQuery = true)
    DailyReportProjection getDailyReport(@Param("storeId") Long storeId, @Param("date") LocalDate date);

    Page<Sale> findAll(Specification<Sale> saleSpecification, Pageable pageable);

    @Query(value = "SELECT nextval('receipt_no_seq')", nativeQuery = true)
    Long getNextReceiptNo();
}
