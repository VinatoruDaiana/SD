package com.example.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class AuthUserShadow {

    @Id
    private UUID id; // același id ca în user-service

    public AuthUserShadow() {}

    public AuthUserShadow(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
}
