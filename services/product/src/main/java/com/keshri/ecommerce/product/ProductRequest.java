package com.keshri.ecommerce.product;

import com.keshri.ecommerce.category.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name is required")
        String name,
        @NotNull(message = "Product description is required")
        String description,
        @Positive(message = "Available quantity should be positive")
        double availableQuantity,
        @Positive(message = "Product price is required")
        BigDecimal price,
        @NotNull(message = "product category is required")
        Integer categoryId
) {



}
