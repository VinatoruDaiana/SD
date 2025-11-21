package com.example.monitoring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthClient {
    private final WebClient web;
    private final String token;

    public AuthClient(WebClient.Builder builder,
                      @Value("${auth.service.baseUrl:http://localhost:8083}") String baseUrl,
                      @Value("${internal.token}") String token) {
        this.web = builder.baseUrl(baseUrl).build();
        this.token = token;
    }

    public void upsertAuth(String username, String email, String passwordHash, String role) {
        var payload = new java.util.HashMap<String,Object>();
        payload.put("username", username);
        payload.put("email", email);
        payload.put("passwordHash", passwordHash);
        payload.put("role", role);

        web.post()
                .uri("/api/auth/internal/sync")
                .header("X-Internal-Token", token)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void deleteAuth(String username) {
        web.delete()
                .uri("/api/auth/internal/sync/{username}", username)
                .header("X-Internal-Token", token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


}
