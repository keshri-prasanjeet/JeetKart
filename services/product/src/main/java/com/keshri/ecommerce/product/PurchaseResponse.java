package com.keshri.ecommerce.product;

import java.math.BigDecimal;

public record PurchaseResponse(
        Integer productId,
        double quantity,
        String name,
        String description,
        BigDecimal price

) {
}
