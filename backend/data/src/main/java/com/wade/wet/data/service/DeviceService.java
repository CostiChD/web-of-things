package com.wade.wet.data.service;

import com.wade.wet.data.model.Device;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private final ModelService modelService;
    private final Model model;
    private final Property deviceNameProperty;

    public DeviceService(ModelService modelService) {
        this.modelService = modelService;
        this.model = modelService.getModel();

        deviceNameProperty = model.createProperty(ModelService.GROUP_URI + "isNamed");
    }

    public Device registerDevice(Device device) {
        model.createResource(ModelService.GROUP_URI + device.getName())
                .addProperty(deviceNameProperty, device.getName());

        modelService.writeModel();
        return device;
    }

}
