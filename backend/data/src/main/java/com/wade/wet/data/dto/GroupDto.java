package com.wade.wet.data.dto;

import com.wade.wet.data.model.Group;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupDto {

    private Group group;

    private String groupAdminEmail;

}
