package com.app.pos.system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
@Table(name = "inventory")
public class Inventory {

    @EmbeddedId
    private InventoryId inventoryId;

    @MapsId("storeId")
    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Store store;

    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "updated_at", nullable = false, insertable = false)
    private OffsetDateTime updatedAt;
}
