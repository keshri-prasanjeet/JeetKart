package com.keshri.ecommerce.order;

import com.keshri.ecommerce.exceptions.BusinessException;
import com.keshri.ecommerce.customer.CustomerClient;
import com.keshri.ecommerce.product.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;//used FeignClient to communicate to customer microservice
    private final ProductClient productClient;//used RestTemplate to communicate to product microservice
    public Integer createOrder(OrderRequest orderRequest) {
        var customer = this.customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order, No customer exists with provided customer id %d"+ orderRequest.customerId()));

        this.productClient.purchaseProducts(orderRequest.products());
    }
}
