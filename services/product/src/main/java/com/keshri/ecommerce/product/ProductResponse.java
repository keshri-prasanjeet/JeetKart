package com.keshri.ecommerce.product;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ProductResponse(
        Integer id,
        String name,
        String description,
        double availableQuantity,
        BigDecimal price,
        Integer categoryId,
        String categoryName,
        String categoryDescription
) {
}
