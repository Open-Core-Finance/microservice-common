package tech.corefinance.common.dto;

import tech.corefinance.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;

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
    private String fistName;
    private String lastName;
    private String displayName;
    private String address;
    private Gender gender;
    private String email;
    private LocalDate birthday;
    private String phoneNumber;
}
