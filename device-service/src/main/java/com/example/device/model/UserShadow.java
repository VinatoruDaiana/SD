package com.example.device.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class UserShadow {

    @Id
    private UUID id;  // același ID ca în user-service

    public UserShadow() {}

    public UserShadow(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
