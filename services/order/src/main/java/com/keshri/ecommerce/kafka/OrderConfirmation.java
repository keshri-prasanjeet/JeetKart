package com.keshri.ecommerce.kafka;

import com.keshri.ecommerce.customer.CustomerResponse;
import com.keshri.ecommerce.order.PaymentMethod;
import com.keshri.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
