package com.keshri.ecommerce.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

// Mark this class as a Spring service component
@Service
// Lombok's annotation to generate a constructor for final fields
@RequiredArgsConstructor
// Lombok annotation to enable logging
@Slf4j
public class OrderNotificationProducer {

    // Inject KafkaTemplate bean, which is used to send messages to Kafka
    // The generic types <String, OrderConfirmation> indicate that the key is a String
    // and the value is an OrderConfirmation object
    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    // Method to send an order confirmation to Kafka
    public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {

        // Log the order confirmation being sent
        log.info("Sending order confirmation: <{}>", orderConfirmation);

        // Create a Message object using MessageBuilder
        // This is a more flexible way to create a message compared to just sending the payload
        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)  // Set the message payload (the actual data)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")  // Set the Kafka topic
                //.setHeader(KafkaHeaders.KEY, orderConfirmation.getOrderId())  Can set the String key like this
                .build();  // Build the message

        // Send the message to Kafka using the KafkaTemplate
        kafkaTemplate.send(message);
    }
}