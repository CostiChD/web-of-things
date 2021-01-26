package com.wade.wet.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMappingList {

    private List<DeviceMapping> deviceMappings;

    public Device getDeviceMappingWithUuid(UUID deviceUuid) {

        for (DeviceMapping deviceMapping : deviceMappings) {
            if (deviceMapping.getDeviceUuid().equals(deviceUuid)) {
                return deviceMapping.getDevice();
            }
        }

        return null;
    }

}
