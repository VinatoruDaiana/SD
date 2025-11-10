package com.example.auth.services;

import java.util.Map;
import java.util.Optional;

import com.example.auth.requests.SyncUserFromUsersService;
import jakarta.transaction.Transactional;
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


        userClient.upsertUser(
                saved.getUsername(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getRole().name()
        );

        return saved.getId();
    }


    public String login(LoginRequest loginRequest) {


        var user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("Username not found"));


        if (!encoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwt.generateAccess(
                user.getUsername(),
                String.valueOf(user.getRole()),
                user.getId());
    }

    public Map<String, Object> validateToken(String token) {
        try {
            String username = jwt.extractUsername(token);
            boolean valid = jwt.isTokenValid(token, username);
            return Map.of("username", username, "valid", valid);
        } catch (Exception e) {
            return Map.of("valid", false, "error", e.getMessage());
        }
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
        // idempotent: dacă nu există, ignore
        userRepository.deleteByUsername(username);
    }
}

