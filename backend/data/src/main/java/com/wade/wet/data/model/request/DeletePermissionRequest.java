package com.wade.wet.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletePermissionRequest {

    private String groupName;

    private String deviceName;

}
