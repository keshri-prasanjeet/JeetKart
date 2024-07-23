package com.keshri.ecommerce.payment;

import com.keshri.ecommerce.customer.CustomerResponse;
import com.keshri.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
//        Customer response because in the order ms we first connect to the customer ms via feign client and then get the
//        information about the customer which is stored as a customer response and then we need customer information again
//        for payment request so this data has to be of type customer response
) {
}
