package com.example.user.services;

import com.example.user.dtos.UserDTO;
import com.example.user.dtos.UserDetailsDTO;
import com.example.user.dtos.builders.UserBuilder;
import com.example.user.entities.User;
import com.example.user.handlers.exceptions.model.CustomException;
import com.example.user.handlers.exceptions.model.ResourceNotFoundException;
import com.example.user.integration.AuthClient;
import com.example.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.user.sync.UserSyncProducer;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthClient authClient;

    private final UserSyncProducer userSyncProducer;


    @Value("${sync.enabled:false}")
    private boolean syncEnabled;

    public UserService(UserRepository repo, PasswordEncoder encoder, AuthClient authClient, UserSyncProducer userSyncProducer) {
        this.repo = repo;
        this.encoder = encoder;
        this.authClient = authClient;

        this.userSyncProducer = userSyncProducer;
    }

    @Transactional
    public UserDTO create(UserDetailsDTO in) {
        if (repo.existsByUsername(in.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User u = new User();
        u.setUsername(in.getUsername());
        u.setEmail(in.getEmail());
        u.setRole(in.getRole());
        u.setPasswordHash(encoder.encode(in.getPassword()));

        u = repo.save(u);



        // Sincronizare DOAR dacă e activată
        if (syncEnabled) {
            try {
                System.out.println("[USER] Syncing to auth-service...");
                authClient.upsertAuth(
                        u.getUsername(),
                        u.getEmail(),
                        u.getPasswordHash(),
                        u.getRole()
                );
                System.out.println("[USER] Sync OK");
            } catch (Exception ex) {
                System.err.println("[USER] Sync failed: " + ex.getMessage());
            }
        }

        userSyncProducer.sendUserCreated(u);

        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getRole()
        );
    }

    public List<UserDTO> getAll() {
        return repo.findAll().stream()
                .map(UserBuilder::toUserDTO)
                .toList();
    }

    public UserDetailsDTO getById(UUID id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserBuilder.toUserDetailsDTO(user);
    }

    @Transactional
    public UserDTO update(UUID id, UserDetailsDTO in) {
        var u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        if (in.getUsername() != null && !in.getUsername().isBlank()
                && !in.getUsername().equals(u.getUsername())) {
            if (repo.existsByUsername(in.getUsername())) {
                throw new IllegalArgumentException("username already exists");
            }
            u.setUsername(in.getUsername());
        }

        if (in.getRole() != null && !in.getRole().isBlank()) {
            u.setRole(in.getRole());
        }

        if (in.getPassword() != null && !in.getPassword().isBlank()) {
            u.setPasswordHash(encoder.encode(in.getPassword()));
        }

        if (in.getEmail() != null && !in.getEmail().isBlank()) {
            u.setEmail(in.getEmail());
        }

        u = repo.save(u);

        // Sincronizare DOAR dacă e activată
        if (syncEnabled) {
            try {
                authClient.upsertAuth(
                        u.getUsername(),
                        u.getEmail(),
                        u.getPasswordHash(),
                        u.getRole()
                );
            } catch (Exception ex) {
                System.err.println("[USER] Sync failed: " + ex.getMessage());
            }
        }

        userSyncProducer.sendUserUpdated(u);
        return new UserDTO(u.getId(), u.getUsername(), u.getRole());
    }

    @Transactional
    public void delete(UUID id) {
        var u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        repo.delete(u);


        userSyncProducer.sendUserDeleted(u);


        // Sincronizare DOAR dacă e activată
        if (syncEnabled) {
            try {
                authClient.deleteAuth(u.getUsername());
            } catch (Exception ex) {
                System.err.println("[USER] Delete sync failed: " + ex.getMessage());
            }
        }
    }

    private void validateRole(String role) {
        if (!"ADMIN".equals(role) && !"CLIENT".equals(role)) {
            throw new CustomException("Role must be ADMIN or CLIENT");
        }
    }

    public UserDTO getByUsername(String username) {
        User u = repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserBuilder.toUserDTO(u);
    }
}