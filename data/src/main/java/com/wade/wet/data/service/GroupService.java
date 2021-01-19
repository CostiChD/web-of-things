package com.wade.wet.data.service;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class GroupService {
    private static final String MODEL_PATH = "group.rdf";
    private Model model;

    @PostConstruct
    void loadModel() {
        model = ModelFactory.createDefaultModel();

        InputStream in = RDFDataMgr.open(MODEL_PATH);
        if (in == null) {
            throw new IllegalArgumentException("File: " + MODEL_PATH + " not found");
        }

        model.read(in, null);
    }

    @PreDestroy
    void writeModel() throws FileNotFoundException {
        OutputStream out = new FileOutputStream(MODEL_PATH);
        model.write(out);
    }
}
