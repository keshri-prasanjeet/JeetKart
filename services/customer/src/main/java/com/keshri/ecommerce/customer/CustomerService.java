package com.keshri.ecommerce.customer;

import com.keshri.ecommerce.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    public String createCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.save(customerMapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Cannot update customer:: No customer found with the provided ID:: %s", customerRequest.id())
                ));
        mergeCustomer(customer, customerRequest);
        customerRepository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest customerRequest) {
        if(StringUtils.isNotBlank(customerRequest.firstName())){
            customer.setFirstName(customerRequest.firstName());
        }
        if(StringUtils.isNotBlank(customerRequest.lastName())){
            customer.setLastName(customerRequest.lastName());
        }
        if(StringUtils.isNotBlank(customerRequest.email())){
            customer.setEmail(customerRequest.email());
        }
        if(customerRequest.address() != null){
            customer.setAddress(customerRequest.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return customerRepository.existsById(customerId);
    }
    public Boolean existsByEmail(String email) {return customerRepository.existsByEmail(email);}
    public CustomerResponse findByCustomerId(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("No customer found with the customer ID: %s",customerId)));
        return customerMapper.toCustomerResponse(customer);
    }

    public void deleteCustomer(String customerId) {
        if(existsById(customerId)){
            customerRepository.deleteById(customerId);
        }
        else throw new CustomerNotFoundException(
                format("No customer found with the customer ID: %s",customerId)
        );
    }
}
