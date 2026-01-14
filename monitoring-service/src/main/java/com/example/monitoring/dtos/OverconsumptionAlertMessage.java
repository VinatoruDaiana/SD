package com.example.monitoring.dtos;

import java.time.LocalDate;
import java.util.UUID;

public class OverconsumptionAlertMessage {
    private UUID deviceId;
    private LocalDate date;
    private int hour;
    private double measuredKwh;
    private double maxAllowedKwh;



    private UUID userId;

    public OverconsumptionAlertMessage() {}

    public OverconsumptionAlertMessage(UUID deviceId, LocalDate date, int hour,
                                       double measuredKwh, double maxAllowedKwh) {
        this.deviceId = deviceId;
        this.date = date;
        this.hour = hour;
        this.measuredKwh = measuredKwh;
        this.maxAllowedKwh = maxAllowedKwh;
    }

    public UUID getDeviceId() { return deviceId; }
    public LocalDate getDate() { return date; }
    public int getHour() { return hour; }
    public double getMeasuredKwh() { return measuredKwh; }
    public double getMaxAllowedKwh() { return maxAllowedKwh; }

    public void setDeviceId(UUID deviceId) { this.deviceId = deviceId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setHour(int hour) { this.hour = hour; }
    public void setMeasuredKwh(double measuredKwh) { this.measuredKwh = measuredKwh; }
    public void setMaxAllowedKwh(double maxAllowedKwh) { this.maxAllowedKwh = maxAllowedKwh; }
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
