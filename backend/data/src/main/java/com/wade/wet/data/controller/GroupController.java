package com.wade.wet.data.controller;

import com.wade.wet.data.model.AddUserToGroupRequest;
import com.wade.wet.data.model.Group;
import com.wade.wet.data.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{groupName}")
    public ResponseEntity<String> getGroupByName(@PathVariable String groupName) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroup(groupName));
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(group));
    }

    @PostMapping("/user")
    public ResponseEntity<Void> addUserToGroup(@RequestBody AddUserToGroupRequest request) {
        groupService.addUserToGroup(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
