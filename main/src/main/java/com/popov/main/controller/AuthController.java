package com.popov.main.controller;

import com.popov.main.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("webflux/auth")
@RequiredArgsConstructor
public class AuthController {

    private final WebClient webClient;

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerUser(@RequestBody User user) {
        String formData = "username=" + user.getUsername() + "&password=" + user.getPassword();
    
        return webClient.post()
                .uri("/register")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    if ("Registration successful".equals(response)) {
                        return ResponseEntity.ok("Registration successful");
                    } else {
                        return ResponseEntity.status(400).body("Registration failed");
                    }
                })
                .onErrorResume(_ -> Mono.just(ResponseEntity.status(500).body("Error occurred during registration")));
    }
    @PostMapping("/login")
    public Mono<ResponseEntity<String>> loginUser(@RequestBody User user) {
        String formData = "username=" + user.getUsername() + "&password=" + user.getPassword();
    
        return webClient.post()
                .uri("/login")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue(formData)
                .retrieve()
                .toEntity(String.class)
                .map(response -> {
                    String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
                    if (sessionId != null) {
                        return ResponseEntity.ok("Login successful. SessionId: " + sessionId);
                    }
                    return ResponseEntity.status(400).body("Login failed");
                })
                .onErrorResume(_ -> Mono.just(ResponseEntity.status(500).body("Error occurred during login")));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<String>> logoutUser(@RequestHeader(HttpHeaders.COOKIE) String sessionId) {
        return webClient.post()
                .uri("/logout")
                .header(HttpHeaders.COOKIE, sessionId) // Send JSESSIONID in the cookies
                .retrieve()
                .bodyToMono(String.class)
                .map(_ -> ResponseEntity.ok("Logout successful"))
                .onErrorReturn(ResponseEntity.status(400).body("Logout failed"));
    }
    
}
