package com.wade.wet.data.service;

import com.wade.wet.data.model.Group;
import com.wade.wet.data.model.request.AddUserToGroupRequest;
import com.wade.wet.data.model.request.CreateGroupRequest;
import org.apache.jena.rdf.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private final ModelService modelService;
    private final Model model;

    public GroupService(ModelService modelService) {
        this.modelService = modelService;
        this.model = modelService.getModel();
    }

    public List<String> getGroupsForUser(String userEmail) {
        List<String> groupNames = new ArrayList<>();

        ResIterator resIterator = getGroupsIterator();

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();

            String modelGroupHasUser = resource.getProperty(modelService.getHasUserProperty()).getObject().toString();
            String modelGroupAdmin = resource.getProperty(modelService.getHasAdminProperty()).getObject().toString();
            String modelGroupName = resource.getProperty(modelService.getGroupNameProperty()).getObject().toString();

            if (modelGroupHasUser.equals(userEmail) || modelGroupAdmin.equals(userEmail)) {
                groupNames.add(modelGroupName);
            }
        }

        return groupNames;
    }

//    public List<Device> getDevicesForGroup(String groupName) {
//        ResIterator resIterator = getGroupsIterator();
//
//        while (resIterator.hasNext()) {
//            Resource resource = resIterator.nextResource();
//
//            String modelGroupName = resource.getProperty(modelService.getGroupNameProperty()).getObject().toString();
//            String modelGroupAdmin = resource.getProperty(modelService.getHasAdminProperty()).getObject().toString();
//
//            if (modelGroupName.equals(groupName) && modelGroupAdmin.equals(request.getAdminEmail())) {
//                resource.addProperty(modelService.getHasUserProperty(), request.getUserEmailToAdd());
//                break;
//            }
//        }
//    }

    public Group createGroup(CreateGroupRequest request) {
        Group group = request.getGroup();
        model.createResource(ModelService.GROUP_URI + group.getName())
                .addProperty(modelService.getGroupNameProperty(), group.getName())
                .addProperty(modelService.getHasAdminProperty(), request.getAdminEmail());

        modelService.writeModel();
        return group;
    }

    public void addUserToGroup(AddUserToGroupRequest request) {
        ResIterator resIterator = getGroupsIterator();

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();

            String modelGroupName = resource.getProperty(modelService.getGroupNameProperty()).getObject().toString();
            String modelGroupAdmin = resource.getProperty(modelService.getHasAdminProperty()).getObject().toString();

            if (modelGroupName.equals(request.getGroupName()) && modelGroupAdmin.equals(request.getAdminEmail())) {
                resource.addProperty(modelService.getHasUserProperty(), request.getUserEmailToAdd());
                break;
            }
        }

        modelService.writeModel();
    }

    private ResIterator getGroupsIterator() {
        return model.listSubjectsWithProperty(modelService.getGroupNameProperty());
    }

    public String getGroup(String groupName) {
        Selector selector = new SimpleSelector(null, modelService.getGroupNameProperty(), groupName);

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
