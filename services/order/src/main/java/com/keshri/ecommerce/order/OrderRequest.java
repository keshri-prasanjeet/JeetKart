package com.keshri.ecommerce.order;

import com.keshri.ecommerce.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
    Integer id,
    String reference,
    @Positive(message = "order amount should be positive")
    BigDecimal amount,
    @NotNull(message = "Payment method should not be null")
    PaymentMethod paymentMethod,
    @NotNull(message = "Customer should be present")
    @NotEmpty(message = "Customer Id should not be empty")
    @NotBlank(message = "Customer Id should not be blank")
    String customerId,
    @NotEmpty(message = "At least one product should be purchased")
    List<PurchaseRequest> products
) {
}
