package com.example.monitoring.repositories;

import com.example.monitoring.entities.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, Long> {

    //folosită de MeasurementProcessingService ca să decidă dacă face UPDATE sau INSERT
    Optional<HourlyConsumption> findByDeviceIdAndDateAndHour(UUID deviceId, LocalDate date, int hour);

    //folosită la citire, pentru grafice
    List<HourlyConsumption> findAllByDeviceIdAndDateOrderByHourAsc(UUID deviceId, LocalDate date);
}
