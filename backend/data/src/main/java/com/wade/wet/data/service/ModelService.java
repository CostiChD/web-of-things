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
    public static final String GROUP_URI = "http://localhost:8082/groups/";

    private Property groupNameProperty;
    private Property hasUserProperty;
    private Property hasAdminProperty;
    private Property hasPermissionProperty;

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
