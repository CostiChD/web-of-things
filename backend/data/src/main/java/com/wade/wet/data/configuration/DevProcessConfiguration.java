package com.wade.wet.data.configuration;

import com.wade.wet.data.auth.Authentication;
import com.wade.wet.data.dto.UserDto;
import com.wade.wet.data.model.Group;
import com.wade.wet.data.model.Permission;
import com.wade.wet.data.model.request.AddUserToGroupRequest;
import com.wade.wet.data.model.request.CreateGroupRequest;
import com.wade.wet.data.model.request.CreatePermissionRequest;
import com.wade.wet.data.service.GroupService;
import com.wade.wet.data.service.PermissionService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class DevProcessConfiguration {

    private final GroupService groupService;
    private final PermissionService permissionService;
    private final Authentication authentication;

    public DevProcessConfiguration(GroupService groupService, PermissionService permissionService,
                                   Authentication authentication) {
        this.groupService = groupService;
        this.permissionService = permissionService;
        this.authentication = authentication;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        String adminEmail = "cristiandragomir323@gmail.com";
        String userEmail = "ionescu.popescu@email.com";
        String groupName = "TestGroup1";
        String deviceName = "DeviceName1";

        UserDto admin = new UserDto("AdminFName", "AdminLName", adminEmail, "parola");
        try {
            authentication.register(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserDto user = new UserDto("UserFName", "UserLName", userEmail, "parola");
        try {
            authentication.register(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Group group = Group.createGroup(groupName);
        groupService.createGroup(new CreateGroupRequest(group, adminEmail));

        groupService.addUserToGroup(new AddUserToGroupRequest(adminEmail, userEmail, groupName));

        Permission permission = new Permission(groupName, deviceName);
        permissionService.createPermission(new CreatePermissionRequest(adminEmail, permission));
    }
}
