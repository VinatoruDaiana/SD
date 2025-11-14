package com.example.auth.services;

import java.util.Map;

import com.example.auth.requests.SyncUserFromUsersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.repositories.AuthRepository;
import com.example.auth.requests.LoginRequest;
import com.example.auth.requests.RegisterRequest;
import com.example.auth.entities.User;
import com.example.auth.entities.UserRole;
import com.example.auth.integration.UserClient;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository userRepository;
    private final PasswordEncoder encoder;
    private final com.example.auth.services.JwtService jwt;
    private final UserClient userClient;

    @Value("${sync.enabled:false}")
    private boolean syncEnabled;

    public Long registerUser(RegisterRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(UserRole.USER)
                .build();

        User saved = userRepository.save(user);

        // Sincronizare DOAR dacă e activată
        if (syncEnabled) {
            try {
                System.out.println("[AUTH] Syncing to user-service...");
                userClient.upsertUser(
                        saved.getUsername(),
                        saved.getEmail(),
                        saved.getPassword(),
                        saved.getRole().name()
                );
                System.out.println("[AUTH] Sync OK");
            } catch (Exception e) {
                System.err.println("[AUTH] Sync failed: " + e.getMessage());
            }
        }

        return saved.getId();
    }


public String login(LoginRequest loginRequest) {
    var user = userRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> new RuntimeException("Username not found"));

    if (!encoder.matches(loginRequest.password(), user.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    // ✅ NO SYNC HERE - just return the token
    return jwt.generateAccess(
            user.getUsername(),
            String.valueOf(user.getRole()),
            user.getId());
}

    @Transactional
    public void upsertFromUsersService(SyncUserFromUsersService in) {
        var u = userRepository.findByUsername(in.username)
                .orElseGet(() -> User.builder().username(in.username).build());

        String email = (in.email == null || in.email.isBlank())
                ? (in.username + "@example.local")
                : in.email;
        u.setEmail(email);

        if (in.passwordHash != null && !in.passwordHash.isBlank()) {
            u.setPassword(in.passwordHash);
        }

        u.setRole(UserRole.fromString(in.role));
        userRepository.save(u);
    }


    @Transactional
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}