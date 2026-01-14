package com.example.device.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "user_shadow")
@Data
public class UserShadow {

    @Id
    private UUID id;
}
