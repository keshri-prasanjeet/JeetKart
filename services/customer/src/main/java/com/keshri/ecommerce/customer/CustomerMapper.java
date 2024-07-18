package com.keshri.ecommerce.customer;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest customerRequest) {

        if (customerRequest == null) {
            return null;
        }

        return Customer.builder()
                .id(customerRequest.id())
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .address(customerRequest.address())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }
}
