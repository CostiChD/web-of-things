package com.wade.wet.data.controller;

import com.wade.wet.data.model.Device;
import com.wade.wet.data.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{deviceName}")
    public ResponseEntity<Device> getDevice(@PathVariable String deviceName) {
        return ResponseEntity.status(HttpStatus.OK).body(deviceService.getDevice(deviceName));
    }

    @PostMapping
    public ResponseEntity<Device> registerDevice(@RequestBody Device device) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.registerDevice(device));
    }

    @DeleteMapping("/{deviceName}")
    public ResponseEntity<String> deleteDevice(@PathVariable String deviceName) {
        return ResponseEntity.status(HttpStatus.OK).body(deviceService.removeDevice(deviceName));
    }

}
