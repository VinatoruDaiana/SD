package com.example.auth.requests;

import com.example.auth.entities.UserRole;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank String username, @NotBlank String email, @NotBlank String password, UserRole userRole) {
}
