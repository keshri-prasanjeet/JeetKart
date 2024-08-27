package com.keshri.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import reactor.core.publisher.Mono;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        ServerRequestCache requestCache = new WebSessionServerRequestCache();

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .requestCache(cache -> cache
                        .requestCache(requestCache)
                )
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/", "/eureka/**", "/login", "/css/**", "/oauth2/**", "/api/v1/customers/create").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            return requestCache.saveRequest(exchange).then(
                                    Mono.fromRunnable(() -> {
                                        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                                        exchange.getResponse().getHeaders().setLocation(exchange.getRequest().getURI().resolve("/login"));
                                    })
                            );
                        })
                )
                .oauth2Login(oauth2 -> oauth2
                        .authenticationSuccessHandler(authenticationSuccessHandler)
                        .authenticationFailureHandler(authenticationFailureHandler())
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public ServerAuthenticationFailureHandler authenticationFailureHandler() {
        return (webFilterExchange, exception) -> {
            webFilterExchange.getExchange().getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        };
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails user = User.withUsername("testUser")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();

        return new MapReactiveUserDetailsService(user);
    }

}