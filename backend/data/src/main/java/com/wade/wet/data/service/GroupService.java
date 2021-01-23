package com.wade.wet.data.service;

import com.wade.wet.data.model.Group;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class GroupService {

    private static final String MODEL_PATH = "group.rdf";
    private static final String GROUP_URI = "http://localhost:8082/groups/";

    private Model model;
    private Property groupName;

    @PostConstruct
    void loadModel() {
        model = ModelFactory.createDefaultModel();
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

        groupName = model.createProperty(GROUP_URI + "/isNamed");
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

    public Group createGroup(Group group) {
        printModel();
        model.createResource(GROUP_URI + group.getName()).addProperty(groupName, group.getName());

        writeModel();
        return group;
    }

    public String getGroup(String groupName) {
        Selector selector = new SimpleSelector(null, this.groupName, groupName);

        StmtIterator iter = model.listStatements(selector);

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.println("1111111");
                return object.toString();
//                System.out.print(object.toString());
            } else {
                // object is a literal
                return object.toString();
//                System.out.print("\"" + object.toString() + "\"");
            }

//            System.out.println(" .");
        }

        return new Group().toString();
    }

    private void printModel() {
        StmtIterator iter = model.listStatements();

        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
        }
    }

}
