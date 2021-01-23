package com.wade.wet.data.service;

import com.wade.wet.data.model.Group;
import com.wade.wet.data.model.request.AddUserToGroupRequest;
import com.wade.wet.data.model.request.CreateGroupRequest;
import com.wade.wet.data.model.response.GetDevicesForGroupResponse;
import com.wade.wet.data.model.response.GetGroupsForUserResponse;
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

    public GetGroupsForUserResponse getGroupsForUser(String userEmail) {
        List<String> groupNames = new ArrayList<>();
        ResIterator resIterator = getGroupsIterator();

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();

            StmtIterator stmtIterator = resource.listProperties(modelService.getHasUserProperty());
            boolean isInGroup = false;

            while (stmtIterator.hasNext() && !isInGroup) {
                if (stmtIterator.nextStatement().getObject().toString().equals(userEmail)) {
                    groupNames.add(resource.getProperty(modelService.getGroupNameProperty()).getObject().toString());
                    isInGroup = true;
                }
            }

            if (!isInGroup) {
                String modelGroupAdmin = resource.getProperty(modelService.getHasAdminProperty()).getObject().toString();

                if (modelGroupAdmin.equals(userEmail)) {
                    groupNames.add(resource.getProperty(modelService.getGroupNameProperty()).getObject().toString());
                }
            }
        }

        return new GetGroupsForUserResponse(groupNames);
    }

//    public GetDevicesForGroupResponse getDevicesForGroup(String groupName) {
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
//
//        return null;
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
