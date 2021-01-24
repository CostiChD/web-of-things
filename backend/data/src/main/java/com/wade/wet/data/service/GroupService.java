package com.wade.wet.data.service;

import com.wade.wet.data.dto.GroupDto;
import com.wade.wet.data.model.Device;
import com.wade.wet.data.model.Group;
import com.wade.wet.data.model.request.AddUserToGroupRequest;
import com.wade.wet.data.model.request.CreateGroupRequest;
import com.wade.wet.data.model.request.GetDevicesRequest;
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
        List<GroupDto> groups = new ArrayList<>();
        ResIterator resIterator = getGroupsIterator();

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();
            String modelGroupAdmin = resource.getProperty(modelService.getHasAdminProperty()).getObject().toString();

            StmtIterator stmtIterator = resource.listProperties(modelService.getHasUserProperty());
            boolean isInGroup = false;

            while (stmtIterator.hasNext() && !isInGroup) {
                String groupUserEmail = stmtIterator.nextStatement().getObject().toString();
                if (groupUserEmail.equals(userEmail)) {
                    String groupName = resource.getProperty(modelService.getGroupNameProperty()).getObject().toString();
                    groups.add(new GroupDto(Group.createGroup(groupName), modelGroupAdmin));
                    isInGroup = true;
                }
            }

            if (!isInGroup && modelGroupAdmin.equals(userEmail)) {
                String groupName = resource.getProperty(modelService.getGroupNameProperty()).getObject().toString();
                groups.add(new GroupDto(Group.createGroup(groupName), modelGroupAdmin));

            }
        }

        return new GetGroupsForUserResponse(groups);
    }

    public GetDevicesForGroupResponse getDevicesForGroup(GetDevicesRequest request) {
        ResIterator resIterator = getGroupsIterator();
        List<Device> devices = new ArrayList<>();

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();
            String modelGroupName = resource.getProperty(modelService.getGroupNameProperty()).getObject().toString();

            if (modelGroupName.equals(request.getGroupName())) {
                StmtIterator stmtIterator = resource.listProperties(modelService.getHasUserProperty());
                boolean isInGroup = false;

                while (stmtIterator.hasNext() && !isInGroup) {
                    if (stmtIterator.nextStatement().getObject().toString().equals(request.getUserEmail())) {
                        devices = getDevicesFromGroupResource(resource);
                        isInGroup = true;
                    }
                }

                if (!isInGroup) {
                    String modelGroupAdmin = resource.getProperty(modelService.getHasAdminProperty()).getObject().toString();

                    if (modelGroupAdmin.equals(request.getUserEmail())) {
                        devices = getDevicesFromGroupResource(resource);
                    }
                }

                break;
            }
        }

        return new GetDevicesForGroupResponse(devices);
    }

    private List<Device> getDevicesFromGroupResource(Resource resource) {
        List<Device> devices = new ArrayList<>();
        StmtIterator stmtIterator = resource.listProperties(modelService.getHasPermissionProperty());

        while (stmtIterator.hasNext()) {
            Device device = new Device();
            device.setName(stmtIterator.nextStatement().getObject().toString());
            devices.add(device);
        }

        return devices;
    }

    public Group createGroup(CreateGroupRequest request) {
        Group group = request.getGroup();
        model.createResource(ModelService.GROUP_URI + group.getName())
                .addProperty(modelService.getGroupNameProperty(), group.getName())
                .addProperty(modelService.getHasAdminProperty(), request.getAdminEmail());

        modelService.writeModel();
        return group;
    }

    public String deleteGroup(String groupName) {
        StmtIterator iterator = model.listStatements();
        List<Statement> statements = new ArrayList<>();

        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();
            if (statement.getSubject().toString().equals(ModelService.GROUP_URI + groupName)) {
                statements.add(statement);
            }
        }

        for (Statement statement : statements) {
            model.remove(statement);
        }

        modelService.writeModel();
        return "Group deleted";
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
