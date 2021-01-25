package com.wade.wet.data.controller;

import com.wade.wet.data.enums.WotActionType;
import com.wade.wet.data.model.Device;
import com.wade.wet.data.model.WotAction;
import com.wade.wet.data.model.WotEvent;
import com.wade.wet.data.model.WotProperty;
import com.wade.wet.data.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{deviceName}")
    public ResponseEntity<Device> getDevice(@PathVariable String deviceName) {

        List<WotProperty> properties = new ArrayList<>();
        properties.add(new WotProperty("status", "ok", "propertieslinnnnnnnnnnnnk"));
        properties.add(new WotProperty("status2", "ok2", "propertieslinnnnnnnnnnnnk2"));

        List<WotAction> actions = new ArrayList<>();
        actions.add(new WotAction("Turn on/off", WotActionType.SWITCHABLE, "actionslinnnnnnnnnnnnk"));

        List<WotEvent> events = new ArrayList<>();
        events.add(new WotEvent("overheating", "The lamp started overheating.", "eventslinnnnnnnnnnnnk"));

        Device device = new Device("Device2", "Device2 description", properties, actions, events);

        return ResponseEntity.status(HttpStatus.OK).body(device);
    }

    @PostMapping
    public ResponseEntity<Device> registerDevice(@RequestBody Device device) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.registerDevice(device));
    }

}
