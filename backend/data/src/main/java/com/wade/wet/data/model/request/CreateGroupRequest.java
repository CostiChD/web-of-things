package com.wade.wet.data.model.request;

import com.wade.wet.data.model.Group;
import lombok.Data;

@Data
public class CreateGroupRequest {

    private Group group;

    private String adminEmail;

}
