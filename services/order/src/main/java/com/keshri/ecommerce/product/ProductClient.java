package com.keshri.ecommerce.product;

import com.keshri.ecommerce.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@Service // Marks this class as a Spring service
@RequiredArgsConstructor // Lombok's annotation to generate a constructor for final fields
public class ProductClient {
    // Injects the product service URL from application properties
    @Value("${application.config.product-url}")
    private String productUrl;

    // RestTemplate is injected via constructor (due to @RequiredArgsConstructor)
    private final RestTemplate restTemplate;

    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> purchaseRequestsBody) {
        // Set up HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create an HTTP entity with the request body and headers
        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(purchaseRequestsBody, headers);

        // Define the response type (a List of PurchaseResponse objects)
        ParameterizedTypeReference<List<PurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() {};

        // Make the HTTP POST request to the product service
        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase", // URL
                POST, // HTTP method
                requestEntity, // Request entity (body + headers)
                responseType // Expected response type
        );

        // Check if the request was successful
        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the products purchase:: " + responseEntity.getStatusCode());
        }

        // Return the response body
        return responseEntity.getBody();
    }
}