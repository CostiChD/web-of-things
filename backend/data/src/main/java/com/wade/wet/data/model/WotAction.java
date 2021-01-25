package com.wade.wet.data.model;

import com.wade.wet.data.enums.WotActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WotAction {

    private String name;

    private WotActionType type;

    private String sourceLink;

}
