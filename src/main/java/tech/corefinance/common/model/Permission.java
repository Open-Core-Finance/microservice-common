package tech.corefinance.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.enums.AccessControl;

@Data
@Document("permission")
@Table(name = "permission")
@Entity
public class Permission implements GenericModel<String> {
    public static final String ANY_ROLE_APPLIED_VALUE = "ANY";
    @Id
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
