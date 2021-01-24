package com.wade.wet.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetDevicesRequest {

    private String userEmail;

    private String groupName;

}
