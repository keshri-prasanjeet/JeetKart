package com.keshri.ecommerce.order;

import com.keshri.ecommerce.exceptions.BusinessException;
import com.keshri.ecommerce.customer.CustomerClient;
import com.keshri.ecommerce.kafka.OrderConfirmation;
import com.keshri.ecommerce.kafka.OrderNotificationProducer;
import com.keshri.ecommerce.orderline.OrderLineRequest;
import com.keshri.ecommerce.orderline.OrderLineService;
import com.keshri.ecommerce.payment.PaymentClient;
import com.keshri.ecommerce.payment.PaymentRequest;
import com.keshri.ecommerce.product.ProductClient;
import com.keshri.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;//used FeignClient to communicate to customer microservice
    private final ProductClient productClient;//used RestTemplate to communicate to product microservice
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderNotificationProducer orderNotificationProducer;
    private final PaymentClient paymentClient;
    public Integer createOrder(OrderRequest orderRequest, HttpHeaders headers) {
        var customer = this.customerClient.findCustomerById(headers, orderRequest.getCustomerId())
                .orElseThrow(() -> new BusinessException(
                        "Cannot create order, No customer exists with provided customer id " + orderRequest.getCustomerId()
                ));

        var purchasedProducts = this.productClient.purchaseProducts(headers, orderRequest.getProducts());
        //makes a purchase by going to product ms and reduces the product inventory

        String reference = UUID.randomUUID().toString();
        OrderRequest orderRequestWithReference = OrderRequest.builder()
                .reference(reference)
                .totalAmount(orderRequest.getTotalAmount())
                .paymentMethod(orderRequest.getPaymentMethod())
                .customerId(orderRequest.getCustomerId())
                .products(orderRequest.getProducts())
                .build();

        log.info("************************");
        log.info(orderRequestWithReference.toString());
        log.info("^^^^^^^^^^^^^^^^^--> === !=");

        Order orderToSave = orderMapper.toOrder(orderRequestWithReference);
        orderToSave.setTotalAmount(orderToSave.getTotalAmount());
        var order = this.orderRepository.save(orderToSave);
        //saving order details in order db

        log.info(order.toString());


        for(PurchaseRequest purchaseRequest:orderRequestWithReference.getProducts()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        //saving order line detail in customer line table

        // payment confirmation
        var paymentRequest = new PaymentRequest(
                orderRequestWithReference.getTotalAmount(),
                orderRequestWithReference.getPaymentMethod(),
                orderRequestWithReference.getId(),
                orderRequestWithReference.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        orderNotificationProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequestWithReference.getReference(),
                        orderRequestWithReference.getTotalAmount(),
                        orderRequestWithReference.getPaymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::fromOrder)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("The requested order [Order no: %d] does not exist", orderId))
                );
    }
}
