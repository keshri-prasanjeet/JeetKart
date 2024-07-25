package com.keshri.ecommerce.payment;

import com.keshri.ecommerce.notification.PaymentNotificationProducer;
import com.keshri.ecommerce.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentNotificationProducer paymentNotificationProducer;

    public Integer createPayment(PaymentRequest paymentRequest) {
        var payment = paymentRepository.save(paymentMapper.toPayment(paymentRequest));
        paymentNotificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        paymentRequest.orderReference(),
                        paymentRequest.amount(),
                        paymentRequest.paymentMethod(),
                        paymentRequest.customer().firstName(),
                        paymentRequest.customer().lastName(),
                        paymentRequest.customer().email()
                )
        );
        return payment.getId();
    }
}
