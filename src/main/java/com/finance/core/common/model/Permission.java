package com.finance.core.common.model;

import com.finance.core.common.enums.AccessControl;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.RequestMethod;

@Data
public class Permission implements GenericModel<String> {
    public static final String ANY_ROLE_APPLIED_VALUE = "ANY";
    @Id
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
}
