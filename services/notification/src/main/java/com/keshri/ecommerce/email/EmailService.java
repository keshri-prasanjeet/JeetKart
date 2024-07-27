package com.keshri.ecommerce.email;

import com.keshri.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.keshri.ecommerce.email.EmailTemplates.ORDER_CONFIRMATION;
import static com.keshri.ecommerce.email.EmailTemplates.PAYMENT_CONFIRMATION;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

// Suppress warnings for similar logging messages
@SuppressWarnings("LoggingSimilarMessage")
// Indicates that this class is a Spring service
@Service
// Lombok's annotation to generate a constructor with required dependencies
@RequiredArgsConstructor
// Lombok annotation to add logging capabilities
@Slf4j
public class EmailService {

    // Dependencies injected by Spring
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    //responsible for processing email templates and inserting dynamic content.

    // Asynchronous method to send payment success email
    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        // Create a new email message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, UTF_8.name());
        // MimeMessageHelper.MULTIPART_MODE_RELATED: This mode is used for sending HTML emails with inline resources (like images).
        // It creates a multipart "related" message, which is appropriate for HTML content with embedded resources.
        messageHelper.setFrom("payments@jeetkart.com");

        // Get the template name for payment confirmation
        final String templateName = PAYMENT_CONFIRMATION.getTemplate();
        log.info("The template name is {}", templateName);
        // Prepare variables for the email template
        Map<String, Object> variable = new HashMap<>();
        variable.put("customerName", customerName);
        variable.put("amount", amount);
        variable.put("orderReference", orderReference);

        // Set up the context for the template engine
        Context context = new Context();
        context.setVariables(variable);
        messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());

        try {
            // Process the template with the given context
            String htmlTemplate = templateEngine.process(templateName, context);
            //template engine takes the html template and takes the values in context and generates new dynamic html with that data
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);

            // Send the email
            mailSender.send(mimeMessage);
            log.info(format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }
    }

    // Asynchronous method to send order confirmation email
    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) throws MessagingException {
        // Create a new email message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("orders@jeetkart.com");

        // Get the template name for order confirmation
        final String templateName = ORDER_CONFIRMATION.getTemplate();

        // Prepare variables for the email template
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("totalAmount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);

        // Set up the context for the template engine
        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(ORDER_CONFIRMATION.getSubject());

        try {
            // Process the template with the given context
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);

            // Send the email
            mailSender.send(mimeMessage);
            log.info(format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }
    }
}