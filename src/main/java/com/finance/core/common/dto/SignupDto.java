package com.finance.core.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private String userEmail;
    private String userId;
    private String username;
    private Collection<UserRoleDto> userRoles;

}
