package com.wade.wet.data.service;

import com.wade.wet.data.model.Permission;
import com.wade.wet.data.model.request.CreatePermissionRequest;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
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

            if (modelGroupName.equals(permission.getGroupName()) && modelGroupAdmin.equals(request.getUserEmail())) {
                resource.addProperty(modelService.getHasPermissionProperty(), permission.getDeviceName());
                break;
            }
        }

        modelService.writeModel();

        return request.getPermission();
    }

}
