package com.app.pos.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class PromotionProductId {

    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "product_id")
    private Long productId;
}
