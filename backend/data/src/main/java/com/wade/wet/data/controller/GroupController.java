package com.wade.wet.data.controller;

import com.wade.wet.data.model.Group;
import com.wade.wet.data.model.request.AddUserToGroupRequest;
import com.wade.wet.data.model.request.CreateGroupRequest;
import com.wade.wet.data.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

//    @GetMapping("/{groupName}")
//    public ResponseEntity<String> getGroupByName(@PathVariable String groupName) {
//        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroup(groupName));
//    }

    @GetMapping("/users/{userEmail}")
    public ResponseEntity<List<String>> getGroupsForUser(@PathVariable String userEmail) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupsForUser(userEmail));
    }

    @GetMapping("/devo-only")
    public ResponseEntity<List<String>> getGroupsTemporaryFunction() {
        return ResponseEntity.status(HttpStatus.OK).body(List.of("TestGroup1", "TestGroup3", "TestGroup4"));
    }

//    @GetMapping("/{groupName}/devices")
//    public ResponseEntity<List<Device>> getDevicesForGroup(@PathVariable String groupName) {
//
//    }

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
