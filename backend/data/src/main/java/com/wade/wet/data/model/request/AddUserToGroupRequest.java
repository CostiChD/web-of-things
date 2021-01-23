package com.wade.wet.data.model.request;

import lombok.Data;

@Data
public class AddUserToGroupRequest {

    private String userEmail;

    private String groupName;

}
