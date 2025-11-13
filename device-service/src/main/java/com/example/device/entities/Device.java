package com.example.device.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double maxConsumption;

    @Column(nullable = false)
    private String description;

    //pt asocierea unui device la un user
    @Column(name = "user_id")
    private UUID userId;

    public Device() {}

    public Device(String name, Double maxConsumption, String description) {
        this.name = name;
        this.maxConsumption = maxConsumption;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(Double maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }


    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxConsumption=" + maxConsumption +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}
