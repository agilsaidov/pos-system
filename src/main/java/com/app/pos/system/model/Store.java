package com.app.pos.system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data
@Table(name = "stores")
@Builder
public class Store {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "opened_at", nullable = false)
    private OffsetDateTime openedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        if (active == null) {
            active = true;
        }
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

}
