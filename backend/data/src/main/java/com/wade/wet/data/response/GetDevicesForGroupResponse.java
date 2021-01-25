package com.wade.wet.data.response;

import com.wade.wet.data.model.Device;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetDevicesForGroupResponse {

    private List<Device> devices;

}
