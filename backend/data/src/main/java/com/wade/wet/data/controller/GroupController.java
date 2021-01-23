package com.wade.wet.data.controller;

import com.wade.wet.data.model.Group;
import com.wade.wet.data.model.request.AddUserToGroupRequest;
import com.wade.wet.data.model.request.CreateGroupRequest;
import com.wade.wet.data.model.response.GetDevicesForGroupResponse;
import com.wade.wet.data.model.response.GetGroupsForUserResponse;
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

    @GetMapping("/{groupName}/devices")
    public ResponseEntity<GetDevicesForGroupResponse> getDevicesForGroup(@PathVariable String groupName) {
        return null;
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

}
