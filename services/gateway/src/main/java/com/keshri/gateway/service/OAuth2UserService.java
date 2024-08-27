//package com.keshri.gateway.service;
//
//import com.keshri.gateway.customer.CustomerRequest;
//import com.keshri.gateway.customer.ReactiveCustomerService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class OAuth2UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final ReactiveCustomerService customerService;
//    private final DefaultReactiveOAuth2UserService delegate = new DefaultReactiveOAuth2UserService();
//
//    @Override
//    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) {
//        return delegate.loadUser(userRequest)
//                .flatMap(oAuth2User -> {
//                    String email = oAuth2User.getAttribute("email");
//                    if (email == null) {
//                        log.error("Email not found in OAuth2User attributes");
//                        return Mono.error(new OAuth2AuthenticationException("Email not found in OAuth2User"));
//                    }
//
//                    return customerService.existsByEmail(email)
//                            .flatMap(exists -> {
//                                if (!exists) {
//                                    CustomerRequest customerRequest = CustomerRequest.builder()
//                                            .firstName(oAuth2User.getAttribute("given_name"))
//                                            .lastName(oAuth2User.getAttribute("family_name"))
//                                            .email(email)
//                                            .build();
//                                    return customerService.createCustomer(customerRequest)
//                                            .doOnSuccess(__ -> log.info("Registered new user with email: {}", email))
//                                            .doOnError(e -> log.error("Failed to create customer: {}", e.getMessage()))
//                                            .thenReturn(oAuth2User);
//                                }
//                                return Mono.just(oAuth2User);
//                            });
//                });
//
//    }
//}