package com.example.devicesimulator.model;

import java.time.Instant;
import java.util.UUID;

// rep in Java un mesaj de masurare
// strctura va fi transformata in JSON si trimisa la RabbitMQ

public class DeviceMeasurementMessage {

    private UUID deviceId;
    private Instant timestamp;
    private double measurementValue; // consum pe interval de 10 minute, in kWh

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

    @Override
    public String toString() {
        return "DeviceMeasurementMessage{" +
                "deviceId=" + deviceId +
                ", timestamp=" + timestamp +
                ", measurementValue=" + measurementValue +
                '}';
    }
}
