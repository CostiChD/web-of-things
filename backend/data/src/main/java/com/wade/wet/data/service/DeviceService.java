package com.wade.wet.data.service;

import com.wade.wet.data.enums.WotActionType;
import com.wade.wet.data.model.Device;
import com.wade.wet.data.model.WotAction;
import com.wade.wet.data.model.WotEvent;
import com.wade.wet.data.model.WotProperty;
import org.apache.jena.rdf.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    private final ModelService modelService;
    private final Model model;

    public DeviceService(ModelService modelService) {
        this.modelService = modelService;
        this.model = modelService.getModel();
    }

    public Device registerDevice(Device device) {
        model.createResource(ModelService.DEVICE_URI + device.getName())
                .addProperty(modelService.getDeviceNameProperty(), device.getName())
                .addProperty(modelService.getDeviceDescriptionProperty(), device.getDescription());

        for (WotProperty property : device.getProperties()) {
            String resourceName = ModelService.DEVICE_URI + device.getName() + "/properties/" + property.getName();
            Resource resource = model.createResource(resourceName);
            resource.addProperty(modelService.getPropertyNameProperty(), property.getName())
                    .addProperty(modelService.getPropertyValueProperty(), property.getValue())
                    .addProperty(modelService.getPropertySourceLinkProperty(), property.getSourceLink());
        }

        for (WotAction action : device.getActions()) {
            String resourceName = ModelService.DEVICE_URI + device.getName() + "/actions/" + action.getName();
            Resource resource = model.createResource(resourceName);
            resource.addProperty(modelService.getActionNameProperty(), action.getName())
                    .addProperty(modelService.getActionTypeProperty(), action.getType().toString())
                    .addProperty(modelService.getActionSourceLinkProperty(), action.getSourceLink());
        }
        for (WotEvent event : device.getEvents()) {
            String resourceName = ModelService.DEVICE_URI + device.getName() + "/events/" + event.getName();
            Resource resource = model.createResource(resourceName);
            resource.addProperty(modelService.getEventNameProperty(), event.getName())
                    .addProperty(modelService.getEventDescriptionProperty(), event.getDescription())
                    .addProperty(modelService.getEventSourceLinkProperty(), event.getSourceLink());
        }

        modelService.writeModel();
        return device;
    }

    public Device getDevice(String deviceName) {
        Selector selector = new SimpleSelector(null, modelService.getDeviceNameProperty(), deviceName);
        StmtIterator iterator = model.listStatements(selector);
        boolean isFound = false;
        String deviceDescription = null;

        while (iterator.hasNext() && !isFound) {
            Statement statement = iterator.nextStatement();


            if (statement.getObject().toString().equals(deviceName)) {
                isFound = true;

                StmtIterator iter = statement.getSubject().listProperties(modelService.getDeviceDescriptionProperty());
                deviceDescription = iter.nextStatement().getObject().toString();
            }
        }

        if (!isFound) {
            return null;
        }

        Device device = new Device();
        device.setName(deviceName);
        device.setDescription(deviceDescription);
        device.setProperties(getDeviceProperties(deviceName));
        device.setActions(getDeviceActions(deviceName));
        device.setEvents(getDeviceEvents(deviceName));

        return device;
    }

    private List<WotProperty> getDeviceProperties(String deviceName) {
        List<WotProperty> properties = new ArrayList<>();
        ResIterator resIterator = model.listSubjectsWithProperty(modelService.getPropertyValueProperty());

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();
            if (resource.toString().contains(ModelService.DEVICE_URI + deviceName + "/")) {
                WotProperty property = new WotProperty();
                property.setName(resource.getProperty(modelService.getPropertyNameProperty()).getObject().toString());
                property.setValue(resource.getProperty(modelService.getPropertyValueProperty()).getObject().toString());
                property.setSourceLink(resource.getProperty(modelService.getPropertySourceLinkProperty()).getObject()
                        .toString());

                properties.add(property);
            }
        }

        return properties;
    }

    private List<WotAction> getDeviceActions(String deviceName) {
        List<WotAction> actions = new ArrayList<>();
        ResIterator resIterator = model.listSubjectsWithProperty(modelService.getActionTypeProperty());

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();
            if (resource.toString().contains(ModelService.DEVICE_URI + deviceName + "/")) {
                WotAction action = new WotAction();
                action.setName(resource.getProperty(modelService.getActionNameProperty()).getObject().toString());
                action.setType(WotActionType.valueOf(
                        resource.getProperty(modelService.getActionTypeProperty()).getObject().toString()
                ));
                action.setSourceLink(resource.getProperty(modelService.getActionSourceLinkProperty()).getObject()
                        .toString());

                actions.add(action);
            }
        }

        return actions;
    }

    private List<WotEvent> getDeviceEvents(String deviceName) {
        List<WotEvent> events = new ArrayList<>();
        ResIterator resIterator = model.listSubjectsWithProperty(modelService.getEventNameProperty());

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();
            if (resource.toString().contains(ModelService.DEVICE_URI + deviceName + "/")) {
                WotEvent event = new WotEvent();
                event.setName(resource.getProperty(modelService.getEventNameProperty()).getObject().toString());
                event.setDescription(resource.getProperty(modelService.getEventDescriptionProperty()).getObject()
                        .toString());
                event.setSourceLink(resource.getProperty(modelService.getEventSourceLinkProperty()).getObject()
                        .toString());

                events.add(event);
            }
        }

        return events;
    }

}
