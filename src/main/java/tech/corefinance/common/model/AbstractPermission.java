package tech.corefinance.common.model;

import org.springframework.data.annotation.Transient;
import tech.corefinance.common.enums.AccessControl;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
public abstract class AbstractPermission implements CreateUpdateDto<String>, GenericModel<String> {
    public static final String ANY_ROLE_APPLIED_VALUE = "ANY";
    @Id
    @jakarta.persistence.Id
    private String id;
    @NotNull
    private String roleId;
    @NotNull
    private String resourceType;
    @NotNull
    private String action;
    private String url;
    @NotNull
    private AccessControl control;
    private RequestMethod requestMethod;

    @Override
    @Transient
    public String getEntityId() {
        return getId();
    }
}
