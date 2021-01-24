package com.wade.wet.data.model;

import lombok.Data;

@Data
public class Group {

    private String name;

    public static Group createGroup(String name) {
        Group group = new Group();

        group.setName(name);

        return group;
    }

}
