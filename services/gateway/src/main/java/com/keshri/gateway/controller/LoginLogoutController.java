package com.keshri.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class LoginLogoutController {

    @GetMapping("/login")
    public Mono<String> loginPage() {
        return Mono.just("customlogin");
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }
}