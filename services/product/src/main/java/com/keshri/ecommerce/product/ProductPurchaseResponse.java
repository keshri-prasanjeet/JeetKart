package com.keshri.ecommerce.product;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Integer productId,
        double quantity,
        String name,
        String description,
        BigDecimal price

) {
}
