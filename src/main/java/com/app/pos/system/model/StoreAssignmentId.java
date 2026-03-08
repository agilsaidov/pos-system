package com.app.pos.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class StoreAssignmentId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "store_id")
    private Long storeId;
}
