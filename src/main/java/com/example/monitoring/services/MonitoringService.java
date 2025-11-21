package com.example.monitoring.services;

import com.example.monitoring.dtos.HourlyConsumptionDTO;
import com.example.monitoring.entities.HourlyConsumption;
import com.example.monitoring.repositories.HourlyConsumptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MonitoringService {

    private final HourlyConsumptionRepository hourlyConsumptionRepository;

    public MonitoringService(HourlyConsumptionRepository hourlyConsumptionRepository) {
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
    }

    public List<HourlyConsumptionDTO> getDailyConsumption(UUID deviceId, LocalDate date) {
        // Luăm tot ce avem în DB
        List<HourlyConsumption> list =
                hourlyConsumptionRepository.findAllByDeviceIdAndDateOrderByHourAsc(deviceId, date);

        Map<Integer, Double> energyByHour = list.stream()
                .collect(Collectors.toMap(
                        HourlyConsumption::getHour,
                        HourlyConsumption::getEnergyKwh
                ));

        // Ne asigurăm că trimitem 24 de puncte (0..23).
        return IntStream.range(0, 24)
                .mapToObj(h -> new HourlyConsumptionDTO(
                        h,
                        energyByHour.getOrDefault(h, 0.0)
                ))
                .collect(Collectors.toList());
    }
}
