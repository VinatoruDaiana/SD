package com.example.monitoring.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hourly_consumption",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_device_date_hour",
                columnNames = {"device_id", "consumption_date", "consumption_hour"}
        ))
public class HourlyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "consumption_date", nullable = false)
    private LocalDate date;

    @Column(name = "consumption_hour", nullable = false)
    private int hour; // 0..23

    @Column(name = "energy_kwh", nullable = false)
    private double energyKwh;

    public HourlyConsumption() {
    }

    public HourlyConsumption(UUID deviceId, LocalDate date, int hour, double energyKwh) {
        this.deviceId = deviceId;
        this.date = date;
        this.hour = hour;
        this.energyKwh = energyKwh;
    }

    public Long getId() {
        return id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getEnergyKwh() {
        return energyKwh;
    }

    public void setEnergyKwh(double energyKwh) {
        this.energyKwh = energyKwh;
    }

    public void addEnergy(double delta) {
        this.energyKwh += delta;
    }
}
