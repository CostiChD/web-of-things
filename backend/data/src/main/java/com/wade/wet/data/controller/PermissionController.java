package com.wade.wet.data.controller;

import com.wade.wet.data.model.Permission;
import com.wade.wet.data.model.request.CreatePermissionRequest;
import com.wade.wet.data.model.request.DeletePermissionRequest;
import com.wade.wet.data.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody CreatePermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(request));
    }

    @DeleteMapping
    public ResponseEntity<String> deletePermission(@PathParam("groupName") String groupName,
                                                   @PathParam("deviceName") String deviceName) {

        DeletePermissionRequest request = new DeletePermissionRequest(groupName, deviceName);
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.deletePermission(request));
    }

}
