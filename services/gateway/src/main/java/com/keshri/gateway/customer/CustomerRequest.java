package com.keshri.gateway.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest{

        String id;
        @NotNull(message = "Customer first name is required")
        String firstName;
        @NotNull(message = "Customer last name is required")
        String lastName;
        @NotNull(message = "Customer Email is required")
        @Email(message = "Valid Email is required")
        String email;
        Address address;
}
