package com.example.device.repositories;

import com.example.device.entities.UserShadow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserShadowRepository extends JpaRepository<UserShadow, UUID> {
}
