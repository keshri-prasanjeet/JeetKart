package com.keshri.ecommerce.product;

import com.keshri.ecommerce.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductClient {
    // Injects the product service URL from application properties
    @Value("${application.config.product-url}")
    private String productUrl;

    // RestTemplate is injected via constructor (due to @RequiredArgsConstructor)
    private final RestTemplate restTemplate;

    public List<PurchaseResponse> purchaseProducts(HttpHeaders incomingHeaders, List<PurchaseRequest> purchaseRequestsBody) {
        // Create new headers, copying the Authorization header if it exists
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String authHeader = incomingHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && !authHeader.isEmpty()) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        }

        // Create an HTTP entity with the request body and headers
        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(purchaseRequestsBody, headers);

        ParameterizedTypeReference<List<PurchaseResponse>> responseType = new ParameterizedTypeReference<>() {};

        // Make the HTTP POST request to the product service
        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                POST,
                requestEntity,
                responseType
        );

        log.info("The response body is " + responseEntity.getBody());

        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the products purchase: " + responseEntity.getStatusCode());
        }

        // Return the response body
        return responseEntity.getBody();
    }
}