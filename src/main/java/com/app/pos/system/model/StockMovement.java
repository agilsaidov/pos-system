package com.app.pos.system.model;

import com.app.pos.system.model.enums.StockMovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
@Builder
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Store store;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

    @Column(name = "type", nullable = false, columnDefinition = "stock_movement_type")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private StockMovementType stockMovementType;

    @Column(name = "qty_delta", nullable = false)
    private Integer qtyDelta;

    @Column(name = "qty_before", nullable = false)
    private Integer qtyBefore;

    @Column(name = "qty_after", nullable = false)
    private Integer qtyAfter;

    @Column(name = "reason", length = 500)
    private String reason;

    @JoinColumn(name = "created_by")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

}
