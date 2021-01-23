package com.wade.wet.data.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetGroupsForUserResponse {

    private List<String> groupNames;

}
