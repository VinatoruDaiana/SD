package com.example.monitoring.controllers;

import com.example.monitoring.dtos.UserDTO;
import com.example.monitoring.dtos.UserDetailsDTO;
import com.example.monitoring.entities.User;
import com.example.monitoring.integration.SyncFromAuthDTO;
import com.example.monitoring.repositories.UserRepository;
import com.example.monitoring.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService service;
    private final UserRepository repo;

    @Value("${internal.token}")
    private String internalToken;

    public UserController(UserService service, UserRepository repo) { this.service = service;
        this.repo = repo;
    }

    @PostMapping("/internal/sync")
    public ResponseEntity<?> internalSync(
            @RequestHeader("X-Internal-Token") String token,
            @RequestBody SyncFromAuthDTO in) {
        if (!internalToken.equals(token)) return ResponseEntity.status(403).body(Map.of("error","forbidden"));

        User u = repo.findByUsername(in.username).orElseGet(User::new);
        u.setUsername(in.username);
        if (in.email != null) u.setEmail(in.email);
        if (in.passwordHash != null && !in.passwordHash.isBlank()) u.setPasswordHash(in.passwordHash);
        if (in.role != null) u.setRole(in.role);
        repo.save(u);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> create(@RequestBody UserDetailsDTO in) {
        System.out.println("[CREATE] " + in.getUsername() + " / " + in.getRole());
        return ResponseEntity.ok(service.create(in));
    }



    @GetMapping
    public List<UserDTO> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserDetailsDTO byId(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable UUID id, @Valid @RequestBody UserDetailsDTO in) {
        return service.update(id, in);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/health")
    public String health() { return "ok"; }

    @RestControllerAdvice
    public class RestErrors {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, Object> onValidation(MethodArgumentNotValidException ex) {
            var errors = ex.getBindingResult().getFieldErrors()
                    .stream().collect(Collectors.toMap(
                            fe -> fe.getField(), fe -> fe.getDefaultMessage(), (a,b)->a));
            return Map.of("error","validation", "fields", errors);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, Object> onIllegalArg(IllegalArgumentException ex) {
            return Map.of("error","bad_request","message",ex.getMessage());
        }
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/by-username/{username}")
    public UserDTO getByUsername(@PathVariable String username) {
        return service.getByUsername(username);
    }



}
