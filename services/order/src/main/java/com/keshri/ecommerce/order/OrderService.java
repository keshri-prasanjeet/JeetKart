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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;//used FeignClient to communicate to customer microservice
    private final ProductClient productClient;//used RestTemplate to communicate to product microservice
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderNotificationProducer orderNotificationProducer;
    private final PaymentClient paymentClient;
    public Integer createOrder(OrderRequest orderRequest) {
        var customer = this.customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() ->
                        new BusinessException(
                                "Cannot create order, No customer exists with provided customer id %d"+ orderRequest.customerId()
                        ));

        var purchasedProducts = this.productClient.purchaseProducts(orderRequest.products());
        //makes a purchase by going to product ms and reduces the product inventory

        var order = this.orderRepository.save(orderMapper.toOrder(orderRequest));
        //saving order details in order db

        for(PurchaseRequest purchaseRequest:orderRequest.products()){
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
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                orderRequest.id(),
                orderRequest.reference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        orderNotificationProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
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
