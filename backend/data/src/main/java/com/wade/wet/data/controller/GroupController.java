package com.wade.wet.data.controller;

import com.wade.wet.data.model.Group;
import com.wade.wet.data.request.AddUserToGroupRequest;
import com.wade.wet.data.request.CreateGroupRequest;
import com.wade.wet.data.request.GetDevicesRequest;
import com.wade.wet.data.response.GetDevicesForGroupResponse;
import com.wade.wet.data.response.GetGroupsForUserResponse;
import com.wade.wet.data.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/users")
    public ResponseEntity<GetGroupsForUserResponse> getGroupsForUser(@PathParam("userEmail") String userEmail) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupsForUser(userEmail));
    }

    @GetMapping("/devices")
    public ResponseEntity<GetDevicesForGroupResponse> getDevicesForGroup(
            @PathParam("userEmail") String userEmail,
            @PathParam("groupName") String groupName) {
        GetDevicesRequest request = new GetDevicesRequest(userEmail, groupName);
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getDevicesForGroup(request));
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(request));
    }

    @PostMapping("/users")
    public ResponseEntity<Void> addUserToGroup(@RequestBody AddUserToGroupRequest request) {
        groupService.addUserToGroup(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{groupName}")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupName) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.deleteGroup(groupName));
    }

}
