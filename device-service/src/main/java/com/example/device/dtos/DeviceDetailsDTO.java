package com.example.device.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

public class DeviceDetailsDTO {

    private UUID id;

    private UUID userId;
    @NotBlank(message = "Name is required")
    private String name;

    @DecimalMin(value = "0.0", inclusive = true)
    @NotNull
    private Double maxConsumption;


    @NotBlank(message = "Description is required")
    private String description;

    public DeviceDetailsDTO() {}

    public DeviceDetailsDTO(String name, Double maxConsumption, String description) {
        this.name = name;
        this.maxConsumption = maxConsumption;
        this.description = description;
    }

    public DeviceDetailsDTO(UUID id, String name, Double maxConsumption, String description) {
        this.id = id;
        this.name = name;
        this.maxConsumption = maxConsumption;
        this.description = description;
    }

    public DeviceDetailsDTO(UUID id, String name, Double maxConsumption, String description, UUID userId) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDetailsDTO that = (DeviceDetailsDTO) o;
        return Objects.equals(name, that.name)
                && Objects.equals(maxConsumption, that.maxConsumption)
                && Objects.equals(description, that.description)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, maxConsumption, description,userId);
    }

    @Override
    public String toString() {
        return "DeviceDetailsDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxConsumption=" + maxConsumption +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
