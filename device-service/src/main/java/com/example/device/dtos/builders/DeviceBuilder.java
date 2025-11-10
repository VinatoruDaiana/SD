package com.example.device.dtos.builders;

import com.example.device.dtos.DeviceDTO;
import com.example.device.dtos.DeviceDetailsDTO;
import com.example.device.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {}

    public static DeviceDTO toDeviceDTO(Device device) {
        if (device == null) return null;
        return new DeviceDTO(
                device.getId(),
                device.getName(),
                device.getMaxConsumption(),
                device.getDescription(),
                device.getUserId()
        );
    }

    public static Device toEntity(DeviceDetailsDTO dto) {
        if (dto == null) return null;
        Device device = new Device();
        device.setId(dto.getId());
        device.setName(dto.getName());
        device.setMaxConsumption(dto.getMaxConsumption());
        device.setDescription(dto.getDescription());
        device.setUserId(dto.getUserId()); // poate fi null (unassigned)
        return device;
    }
}
