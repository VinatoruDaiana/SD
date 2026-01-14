package com.example.auth.controllers;

import com.example.auth.requests.SyncUserFromUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auth.requests.LoginRequest;
import com.example.auth.requests.RegisterRequest;
import com.example.auth.services.AuthService;

import org.springframework.beans.factory.annotation.Value;
import java.util.Objects;

import java.util.Map;


@Tag(name = "Auth")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService userService;
    private final AuthService authService;

    @Value("${internal.token}")
    private String internalToken;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request);
            return ResponseEntity.ok(Map.of("message", "registered"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Tag(name = "Auth")
    @Operation(summary = "Login", description = "Primești un JWT pentru apeluri ulterioare")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(Map.of("token", userService.login(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token) {
        try {
            return ResponseEntity.ok(userService.validateToken(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("ok", "auth");
    }

    @PostMapping("/internal/sync")
    public ResponseEntity<?> internalSync(
            @RequestHeader("X-Internal-Token") String token,
            @RequestBody SyncUserFromUsersService body) {


        String it = internalToken == null ? null : internalToken.trim();
        String ht = token == null ? null : token.trim();


        System.out.println("[INTERNAL SYNC] internal.token=" + it);
        System.out.println("[INTERNAL SYNC] header token=" + ht);

        if (!Objects.equals(it, ht)) {
            return ResponseEntity.status(403).body(Map.of("error", "bad internal token"));
        }

        authService.upsertFromUsersService(body);
        return ResponseEntity.ok(Map.of("ok", true));
    }




    @DeleteMapping("/internal/sync/{username}")
    public ResponseEntity<?> internalDelete(
            @RequestHeader("X-Internal-Token") String token,
            @PathVariable String username) {

        if (!Objects.equals(
                internalToken == null ? null : internalToken.trim(),
                token == null ? null : token.trim())) {
            return ResponseEntity.status(403).body(Map.of("error","bad internal token"));
        }

        authService.deleteByUsername(username);
        return ResponseEntity.noContent().build(); // 204 idempotent
    }


}
