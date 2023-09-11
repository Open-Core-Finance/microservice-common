package tech.corefinance.common.test.support.model;

import java.util.Date;

import lombok.Data;

@Data
public class RoleTest {

    private String name;

    private RoleGroupTest group;

    private Date createDate;

    public void setId(String id) {
        setName(id);
    }

    public String getId() {
        return getName();
    }
}
