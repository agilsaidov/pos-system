package com.app.pos.system.dto.request;

@Getter
public class CheckoutRequest {

    @NotNull(message = "Store is required")
    private Long storeId;

    @NotEmpty(message = "Items cannot be empty")
    private List<CheckoutItemRequest> items;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Amount tendered is required")
    @DecimalMin(value = "0.01", message = "Amount tendered must be positive")
    private BigDecimal amountTendered;
}