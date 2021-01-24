package com.wade.wet.data.service;

import com.wade.wet.data.model.Permission;
import com.wade.wet.data.model.request.CreatePermissionRequest;
import com.wade.wet.data.model.request.DeletePermissionRequest;
import org.apache.jena.rdf.model.*;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final ModelService modelService;
    private final Model model;

    public PermissionService(ModelService modelService) {
        this.modelService = modelService;
        this.model = modelService.getModel();
    }

    public Permission createPermission(CreatePermissionRequest request) {
        Permission permission = request.getPermission();
        ResIterator resIterator = model.listSubjectsWithProperty(modelService.getGroupNameProperty());

        while (resIterator.hasNext()) {
            Resource resource = resIterator.nextResource();

            String modelGroupName = resource.getProperty(modelService.getGroupNameProperty()).getObject().toString();
            String modelGroupAdmin = resource.getProperty(modelService.getHasAdminProperty()).getObject().toString();

            if (modelGroupName.equals(permission.getGroupName()) && modelGroupAdmin.equals(request.getAdminEmail())) {
                resource.addProperty(modelService.getHasPermissionProperty(), permission.getDeviceName());
                break;
            }
        }

        modelService.writeModel();
        return request.getPermission();
    }

    public String deletePermission(DeletePermissionRequest request) {
        Selector selector = new SimpleSelector(null, modelService.getHasPermissionProperty(), request.getDeviceName());
        StmtIterator iterator = model.listStatements(selector);
        Statement statement = null;

        while (iterator.hasNext()) {
            Statement stmt = iterator.nextStatement();

            if (stmt.getSubject().toString().equals(ModelService.GROUP_URI + request.getGroupName())) {
                statement = stmt;
            }
        }

        if (statement != null) {
            model.remove(statement);
        }
        modelService.writeModel();
        return "Permission deleted";
    }

}
