package tech.corefinance.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto implements GrantedAuthority {
    private String resourceType;
    private String resourceId;
    @NotEmpty(message = "roleId_must_not_null")
    private String roleId;
    private String roleName;

    public UserRoleDto(String resourceType, String resourceId, String roleId) {
        this(resourceType, resourceId, roleId, "NA");
    }

    /**
     * Importance constructor for JSON deserializer.
     * @param jsonString RoleInSchoolDto JSON
     */
    @SneakyThrows({JsonProcessingException.class})
    public UserRoleDto(String jsonString) {
        UserRoleDto that = new ObjectMapper().readValue(jsonString, getClass());
        this.resourceId = that.resourceId;
        this.resourceType = that.resourceType;
        this.roleId = that.roleId;
        this.roleName = that.roleName;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return resourceType + "_" + resourceId + "_" + roleId;
    }
}
