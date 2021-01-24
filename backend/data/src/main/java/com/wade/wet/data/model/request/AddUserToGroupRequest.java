package com.wade.wet.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserToGroupRequest {

    private String adminEmail;

    private String userEmailToAdd;

    private String groupName;

}
