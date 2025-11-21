package com.example.monitoring.services;

import com.example.monitoring.dtos.UserDTO;
import com.example.monitoring.dtos.UserDetailsDTO;
import com.example.monitoring.dtos.builders.UserBuilder;
import com.example.monitoring.entities.User;
import com.example.monitoring.handlers.exceptions.model.CustomException;
import com.example.monitoring.handlers.exceptions.model.ResourceNotFoundException;
import com.example.monitoring.integration.AuthClient;
import com.example.monitoring.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthClient authClient;

    public UserService(UserRepository repo, PasswordEncoder encoder, AuthClient authClient) {
        this.repo = repo;
        this.encoder = encoder;
        this.authClient = authClient;
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


        try {
            System.out.println("[SYNC→AUTH] " + u.getUsername());
            authClient.upsertAuth(
                    u.getUsername(),
                    u.getEmail(),
                    u.getPasswordHash(),
                    u.getRole()
            );
            System.out.println("[SYNC→AUTH] OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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


        try {
            authClient.upsertAuth(
                    u.getUsername(),
                    u.getEmail(),
                    u.getPasswordHash(),
                    u.getRole()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new UserDTO(u.getId(), u.getUsername(), u.getRole());
    }



    @Transactional
    public void delete(UUID id) {
        var u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));


        repo.delete(u);


        try {
            authClient.deleteAuth(u.getUsername());
        } catch (Exception ex) {
            ex.printStackTrace();
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
