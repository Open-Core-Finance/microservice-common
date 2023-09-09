package com.finance.core.common.test.support.model;

import com.finance.core.common.model.GenericModel;

import java.util.Map;
import java.util.Objects;

public class PermissionTest implements GenericModel<String> {

    private String id;
    private String name;

    private Map<String, String> nameInLanguages;

    private String description;

    public PermissionTest() {
        // Default constructor.
    }

    public PermissionTest(String id) {
        this.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getNameInLanguages() {
        return nameInLanguages;
    }

    public void setNameInLanguages(Map<String, String> nameInLanguages) {
        this.nameInLanguages = nameInLanguages;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"className\": \"").append("Permission").append('\"');
        sb.append(", \"name\": \"").append(name).append('\"');
        sb.append(", \"nameInLanguages\": \"").append(nameInLanguages).append('\"');
        sb.append(", \"id\": ").append(id);
        sb.append(", \"description\": ").append(description);
        sb.append('}');
        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        PermissionTest that = (PermissionTest) o;
        return getId().equals(that.getId());
    }

    @Override public int hashCode() {
        return Objects.hash(getId());
    }

}
