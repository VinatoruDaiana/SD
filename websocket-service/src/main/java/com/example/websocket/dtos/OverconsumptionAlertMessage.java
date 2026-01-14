package com.example.websocket.dtos;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Mesajul trimis de monitoring-service către websocket-service în RabbitMQ,
 * atunci când se detectează overconsumption (consum > prag).
 */
public class OverconsumptionAlertMessage {

    private UUID deviceId;
    private LocalDate date;
    private int hour; // 0..23
    private double measuredKwh;
    private double maxAllowedKwh;

    private UUID userId;

    public OverconsumptionAlertMessage() {
    }

    public OverconsumptionAlertMessage(UUID deviceId, LocalDate date, int hour,
                                      double measuredKwh, double maxAllowedKwh) {
        this.deviceId = deviceId;
        this.date = date;
        this.hour = hour;
        this.measuredKwh = measuredKwh;
        this.maxAllowedKwh = maxAllowedKwh;
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

    public double getMeasuredKwh() {
        return measuredKwh;
    }

    public void setMeasuredKwh(double measuredKwh) {
        this.measuredKwh = measuredKwh;
    }

    public double getMaxAllowedKwh() {
        return maxAllowedKwh;
    }

    public void setMaxAllowedKwh(double maxAllowedKwh) {
        this.maxAllowedKwh = maxAllowedKwh;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "OverconsumptionAlertMessage{" +
                "deviceId=" + deviceId +
                ", date=" + date +
                ", hour=" + hour +
                ", measuredKwh=" + measuredKwh +
                ", maxAllowedKwh=" + maxAllowedKwh +
                '}';
    }
}
