package com.wade.wet.data.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Setter
@Getter
@Service
public class ModelService {

    public static final String MODEL_PATH = "group.rdf";
    public static final String GROUP_URI = "http://ec2-18-156-163-248.eu-central-1.compute.amazonaws.com/groups/";
    public static final String DEVICE_URI = "http://ec2-18-156-163-248.eu-central-1.compute.amazonaws.com/devices/";
    public static final String DEVICE_PROPERTY_URI = "http://ec2-18-156-163-248.eu-central-1.compute.amazonaws.com/devices/properties/";
    public static final String DEVICE_ACTION_URI = "http://ec2-18-156-163-248.eu-central-1.compute.amazonaws.com/devices/actions/";
    public static final String DEVICE_EVENT_URI = "http://ec2-18-156-163-248.eu-central-1.compute.amazonaws.com/devices/events/";

    private Property groupNameProperty;
    private Property hasUserProperty;
    private Property hasAdminProperty;
    private Property hasPermissionProperty;
    private Property deviceNameProperty;
    private Property deviceDescriptionProperty;
    private Property propertyNameProperty;
    private Property propertyValueProperty;
    private Property propertySourceLinkProperty;
    private Property actionNameProperty;
    private Property actionTypeProperty;
    private Property actionSourceLinkProperty;
    private Property eventNameProperty;
    private Property eventDescriptionProperty;
    private Property eventSourceLinkProperty;

    public Property getGroupNameProperty() {
        return groupNameProperty;
    }

    public Property getHasUserProperty() {
        return hasUserProperty;
    }

    public Property getHasAdminProperty() {
        return hasAdminProperty;
    }

    public Property getHasPermissionProperty() {
        return hasPermissionProperty;
    }

    private Model model;

    @PostConstruct
    void loadModel() {
        model = ModelFactory.createDefaultModel();

        groupNameProperty = model.createProperty(ModelService.GROUP_URI + "isNamed");
        hasUserProperty = model.createProperty(ModelService.GROUP_URI + "hasUser");
        hasAdminProperty = model.createProperty(ModelService.GROUP_URI + "hasAdmin");
        hasPermissionProperty = model.createProperty(ModelService.GROUP_URI + "hasPermission");
        deviceNameProperty = model.createProperty(ModelService.DEVICE_URI + "isNamed");
        deviceDescriptionProperty = model.createProperty(ModelService.DEVICE_URI + "hasDescription");
        propertyNameProperty = model.createProperty(ModelService.DEVICE_PROPERTY_URI + "isNamed");
        propertyValueProperty = model.createProperty(ModelService.DEVICE_PROPERTY_URI + "hasValue");
        propertySourceLinkProperty = model.createProperty(ModelService.DEVICE_PROPERTY_URI + "hasSourceLink");
        actionNameProperty = model.createProperty(ModelService.DEVICE_ACTION_URI + "isNamed");
        actionTypeProperty = model.createProperty(ModelService.DEVICE_ACTION_URI + "isOfType");
        actionSourceLinkProperty = model.createProperty(ModelService.DEVICE_ACTION_URI + "hasSourceLink");
        eventNameProperty = model.createProperty(ModelService.DEVICE_EVENT_URI + "isNamed");
        eventDescriptionProperty = model.createProperty(ModelService.DEVICE_EVENT_URI + "hasDescription");
        eventSourceLinkProperty = model.createProperty(ModelService.DEVICE_EVENT_URI + "hasSourceLink");

        OutputStream out = null;
        try {
            out = new FileOutputStream(MODEL_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(out);

        InputStream in = RDFDataMgr.open(MODEL_PATH);
        if (in == null) {
            throw new IllegalArgumentException("File: " + MODEL_PATH + " not found");
        }

        model.read(in, null);
    }

    void writeModel() {
        OutputStream out = null;
        try {
            out = new FileOutputStream(MODEL_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        model.write(out);
    }

}
