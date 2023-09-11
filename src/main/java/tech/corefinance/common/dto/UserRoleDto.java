package tech.corefinance.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.corefinance.common.ex.ServiceProcessingException;
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

    /**
     * Importance constructor for JSON deserializer.
     * @param jsonString RoleInSchoolDto JSON
     */
    public UserRoleDto(String jsonString) {
        try {
            UserRoleDto that = new ObjectMapper().readValue(jsonString, getClass());
            this.resourceId = that.resourceId;
            this.resourceType = that.resourceType;
            this.roleId = that.roleId;
        } catch (JsonProcessingException e) {
            throw new ServiceProcessingException("Parse version error", e);
        }
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return resourceType + "_" + resourceId + "_" + roleId;
    }
}
