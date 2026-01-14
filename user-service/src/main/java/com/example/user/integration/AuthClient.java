package com.example.user.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class AuthClient {
    private final WebClient web;
    private final String token;

    public AuthClient(WebClient.Builder builder,
                      @Value("${auth.service.baseUrl:http://localhost:8083}") String baseUrl,
                      @Value("${internal.token}") String token) {
        this.web = builder
                .baseUrl(baseUrl)
                .build();
        this.token = token;
    }

    public void upsertAuth(String username, String email, String passwordHash, String role) {
        var payload = new java.util.HashMap<String,Object>();
        payload.put("username", username);
        payload.put("email", email);
        payload.put("passwordHash", passwordHash);
        payload.put("role", role);

        try {
            web.post()
                    .uri("/api/auth/internal/sync")
                    .header("X-Internal-Token", token)
                    .bodyValue(payload)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(5))  // Add timeout
                    .block();
        } catch (Exception e) {
            System.err.println("Sync to auth-service failed: " + e.getMessage());
            // Don't throw - just log
        }
    }

    public void deleteAuth(String username) {
        try {
            web.delete()
                    .uri("/api/auth/internal/sync/{username}", username)
                    .header("X-Internal-Token", token)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(5))  // Add timeout
                    .block();
        } catch (Exception e) {
            System.err.println("Delete from auth-service failed: " + e.getMessage());
        }
    }
}