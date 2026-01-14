package com.example.device.repositories;

import com.example.device.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface DeviceRepository extends JpaRepository<Device, UUID> {


    boolean existsByName(String name);


    Optional<Device> findByName(String name);


    List<Device> findByMaxConsumption(Double maxConsumption);

    List<Device> findByUserId(UUID userId);
}
