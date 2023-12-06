package tech.corefinance.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasicUserDto implements Serializable {
    private static final long serialVersionUID = 3390472232987586906L;
    private String userId;
    @NotBlank(message = "user_username_empty")
    private String username;
    @NotBlank(message = "user_email_empty")
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String displayName;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"className\": \"").append(getClass().getSimpleName()).append('\"');
        sb.append(", \"userId\": \"").append(userId).append('\"');
        sb.append(", \"username\": \"").append(username).append('\"');
        sb.append(", \"email\": \"").append(email).append('\"');
        sb.append(", \"firstName\": \"").append(firstName).append('\"');
        sb.append(", \"middleName\": \"").append(middleName).append('\"');
        sb.append(", \"lastName\": \"").append(lastName).append('\"');
        sb.append(", \"displayName\": \"").append(displayName).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
