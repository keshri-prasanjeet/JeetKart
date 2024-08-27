package com.keshri.gateway.security;

import com.keshri.gateway.customer.CustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final String customerUrl;
    private final WebClient.Builder webClientBuilder;
    private final ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
    private final ServerRequestCache requestCache = new WebSessionServerRequestCache();

    public CustomAuthenticationSuccessHandler(@Value("${application.config.customer-url}") String customerUrl,
            WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.customerUrl = customerUrl;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        logger.debug("Authentication success. customerUrl = {}", customerUrl);

        ServerWebExchange exchange = webFilterExchange.getExchange();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauthUser = ((OAuth2AuthenticationToken) authentication).getPrincipal();
            String email = oauthUser.getAttribute("email");
            String name = oauthUser.getAttribute("name");
            if (email == null || name == null) {
                logger.error("Email or name is null for authenticated user");
                return redirectToSavedRequest(exchange);
            }
            String[] fullName = name.split(" ");
            WebClient webClient = webClientBuilder.build();
            return webClient.get()
                    .uri(customerUrl + "/exists-by-email/{email}", email)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(exists -> {
                        if (!exists) {
                            logger.info("Creating new customer for email: {}", email);
                            CustomerRequest customerRequest = CustomerRequest.builder()
                                    .email(email)
                                    .firstName(fullName[0])
                                    .lastName(fullName.length > 1 ? fullName[1] : "")
                                    .build();
                            return createCustomer(webClient, customerRequest);
                        }
                        return Mono.empty();
                    })
                    .then(redirectToSavedRequest(exchange))
                    .onErrorResume(e -> {
                        logger.error("Error during customer check/creation", e);
                        return redirectToSavedRequest(exchange);
                    });
        }

        return redirectToSavedRequest(exchange);
    }

    private Mono<Void> redirectToSavedRequest(ServerWebExchange exchange) {
        return this.requestCache.getRedirectUri(exchange)
                .switchIfEmpty(Mono.just(URI.create("/")))  // Default to home page if no saved request
                .flatMap(uri -> {
                    logger.debug("Redirecting to: {}", uri);
                    return this.redirectStrategy.sendRedirect(exchange, uri);
                });
    }

    private Mono<String> createCustomer(WebClient webClient, CustomerRequest customerRequest) {

        return webClient.post()
                .uri(customerUrl + "/create")
                .bodyValue(customerRequest)
                .retrieve()
                .bodyToMono(String.class);
    }
}