package com.example.monitoring.dtos;

public class HourlyConsumptionDTO {

    private int hour;        // 0..23
    private double energyKwh;

    public HourlyConsumptionDTO() {
    }

    public HourlyConsumptionDTO(int hour, double energyKwh) {
        this.hour = hour;
        this.energyKwh = energyKwh;
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
}
