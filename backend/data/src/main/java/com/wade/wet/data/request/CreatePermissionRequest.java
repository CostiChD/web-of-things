package com.wade.wet.data.request;

import com.wade.wet.data.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionRequest {

    private String adminEmail;

    private Permission permission;

}
