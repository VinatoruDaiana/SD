package com.example.monitoring.dtos;

import java.time.Instant;
import java.util.UUID;

/**
 * Structura mesajului trimis de Device Data Simulator în RabbitMQ.
 */
public class DeviceMeasurementMessage {

    private UUID deviceId;
    private Instant timestamp;
    private double measurementValue; // consum pe interval de 10 minute, în kWh

    public DeviceMeasurementMessage() {
    }

    public DeviceMeasurementMessage(UUID deviceId, Instant timestamp, double measurementValue) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.measurementValue = measurementValue;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(double measurementValue) {
        this.measurementValue = measurementValue;
    }
}
