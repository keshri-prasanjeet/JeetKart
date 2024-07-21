package com.keshri.ecommerce.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated//the reason why this class is marked as validated: because it is part of PaymentRequest DTO record
//and PaymentRequest in payment controller -> createPayment has the notation @Valid
public record Customer(
    String id,
    @NotNull(message = "Firstname is required")
    String firstName,
    @NotNull(message = "Lastname is required")
    String lastName,
    @NotNull(message = "Email is required")
    @Email(message = "Please enter a valid email")
    String email
) {
}
