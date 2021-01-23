package com.wade.wet.data.model.request;

import com.wade.wet.data.model.Permission;
import lombok.Data;

@Data
public class CreatePermissionRequest {

    private String adminEmail;

    private Permission permission;

}
