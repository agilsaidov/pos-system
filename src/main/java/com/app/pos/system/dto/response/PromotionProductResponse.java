package com.app.pos.system.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionProductResponse {
    private Long productId;
    private String productName;
    private String barcode;
    private String active;
}