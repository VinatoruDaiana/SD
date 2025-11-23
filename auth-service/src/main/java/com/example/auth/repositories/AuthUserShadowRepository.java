package com.example.auth.repositories;

import com.example.auth.model.AuthUserShadow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthUserShadowRepository extends JpaRepository<AuthUserShadow, UUID> {
}
