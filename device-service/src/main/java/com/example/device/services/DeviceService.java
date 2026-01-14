package com.example.device.services;

import com.example.device.dtos.DeviceDTO;
import com.example.device.dtos.DeviceDetailsDTO;
import com.example.device.dtos.builders.DeviceBuilder;
import com.example.device.entities.Device;
import com.example.device.handlers.exceptions.model.CustomException;
import com.example.device.handlers.exceptions.model.ResourceNotFoundException;
import com.example.device.repositories.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.device.sync.DeviceSyncProducer;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository repo;
    private final DeviceSyncProducer deviceSyncProducer;

    public DeviceService(DeviceRepository repo, DeviceSyncProducer deviceSyncProducer) {
        this.repo = repo;

        this.deviceSyncProducer = deviceSyncProducer;
    }




    @Transactional
    public DeviceDTO create(DeviceDetailsDTO in) {
        if (repo.existsByName(in.getName())) {
            throw new CustomException("Device name already exists");
        }


        Device device = DeviceBuilder.toEntity(in);
        device = repo.save(device);

        deviceSyncProducer.sendDeviceCreated(device);


        return DeviceBuilder.toDeviceDTO(device);
    }



    public List<DeviceDTO> getAll() {
        return repo.findAll().stream()
                .map(DeviceBuilder::toDeviceDTO)
                .toList();
    }


    @Transactional(readOnly = true)
    public DeviceDTO getById(UUID id) {
        Device d = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        return DeviceBuilder.toDeviceDTO(d);
    }


    @Transactional
    public DeviceDTO update(UUID id, DeviceDetailsDTO in) {
        Device device = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));

        if (!device.getName().equals(in.getName()) && repo.existsByName(in.getName())) {
            throw new CustomException("Device name already exists");
        }

        device.setName(in.getName());
        device.setMaxConsumption(in.getMaxConsumption());
        device.setDescription(in.getDescription());

        device = repo.save(device);

        deviceSyncProducer.sendDeviceUpdated(device);

        return DeviceBuilder.toDeviceDTO(device);
    }


    @Transactional
    public void delete(UUID id) {
        Device device = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));

        repo.delete(device);

        deviceSyncProducer.sendDeviceDeleted(device);
    }


    @Transactional
    public DeviceDTO assign(UUID deviceId, UUID userId) {
        Device d = repo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        d.setUserId(userId);
        d = repo.saveAndFlush(d); // forțează persist + return corect
        return DeviceBuilder.toDeviceDTO(d);
    }

    @Transactional
    public DeviceDTO unassign(UUID deviceId) {
        Device d = repo.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        d.setUserId(null);
        d = repo.saveAndFlush(d);
        return DeviceBuilder.toDeviceDTO(d);
    }
    public List<DeviceDTO> getByUser(UUID userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(DeviceBuilder::toDeviceDTO)
                .toList();
    }


}
