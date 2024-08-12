package com.keshri.ecommerce.order;

import com.keshri.ecommerce.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Integer id;
    private String reference;

    @Positive(message = "order amount should be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method should not be null")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Customer should be present")
    @NotEmpty(message = "Customer Id should not be empty")
    @NotBlank(message = "Customer Id should not be blank")
    private String customerId;

    @NotEmpty(message = "At least one product should be purchased")
    private List<PurchaseRequest> products;

}