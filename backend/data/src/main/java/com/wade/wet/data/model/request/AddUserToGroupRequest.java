package com.wade.wet.data.model.request;

import lombok.Data;

@Data
public class AddUserToGroupRequest {

    private String adminEmail;

    private String userEmailToAdd;

    private String groupName;

}
