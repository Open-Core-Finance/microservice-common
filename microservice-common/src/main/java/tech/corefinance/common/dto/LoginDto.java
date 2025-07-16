package tech.corefinance.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String loginId;
    private String userId;
    private String token;
    private String refreshToken;
    private String username;
    private String userEmail;
    private Collection<UserRoleDto> userRoles;
    private String firstName;
    private String lastName;
    private String displayName;
    private String address;
    private String gender;
    private String email;
    private LocalDate birthday;
    private String phoneNumber;
    private Map<String, Object> additionalInfo;
}
