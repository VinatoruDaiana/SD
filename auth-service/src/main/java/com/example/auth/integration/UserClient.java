package com.example.auth.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class UserClient {
    private final WebClient web;
    private final String token;

    public UserClient(
            WebClient.Builder builder,
            @Value("${user.service.baseUrl:http://localhost:8081}") String baseUrl,
            @Value("${internal.token}") String token) {
        this.web = builder
                .baseUrl(baseUrl)
                .build();
        this.token = token;
    }

    public void upsertUser(String username, String email, String passwordHash, String role) {
        var payload = new java.util.HashMap<String,Object>();
        payload.put("username", username);
        payload.put("email", email);
        payload.put("passwordHash", passwordHash);
        payload.put("role", role);
        
        try {
            web.post()
                    .uri("/api/users/internal/sync")
                    .header("X-Internal-Token", token)
                    .bodyValue(payload)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(5))  // Add timeout
                    .block();
        } catch (Exception e) {
            System.err.println("Sync to user-service failed: " + e.getMessage());
            // Don't throw - just log
        }
    }
}