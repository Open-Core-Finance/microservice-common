package tech.corefinance.common.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.model.Permission;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.repository.PermissionRepository;

public interface PermissionService extends CommonService<String, Permission, PermissionRepository> {

    /**
     * Create ResourceAction.
     * @param resourceType Resource type name.
     * @param action Action name.
     * @param url URL.
     * @param requestMethod Request Method.
     * @return New ResourceAction object.
     */
    default @NotNull ResourceAction newResourceAction(String resourceType, String action, String url,
                                                      RequestMethod requestMethod) {
        return new ResourceAction(resourceType, action, url, requestMethod);
    }

    /**
     * Create Permission.
     * @return New Permission object.
     */
    default @NotNull Permission createEntityObject() {
        return new Permission();
    }
}
