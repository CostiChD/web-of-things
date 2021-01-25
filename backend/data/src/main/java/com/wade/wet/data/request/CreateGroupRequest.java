package com.wade.wet.data.request;

import com.wade.wet.data.model.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest {

    private Group group;

    private String adminEmail;

}
