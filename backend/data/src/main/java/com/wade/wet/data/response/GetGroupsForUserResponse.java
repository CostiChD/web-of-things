package com.wade.wet.data.response;

import com.wade.wet.data.dto.GroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetGroupsForUserResponse {

    private List<GroupDto> groups;

}
