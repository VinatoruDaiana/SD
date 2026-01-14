package com.example.monitoring.repositories;

import com.example.monitoring.entities.DeviceShadow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceShadowRepository extends JpaRepository<DeviceShadow, UUID> {
}
