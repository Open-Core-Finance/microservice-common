package tech.corefinance.userprofile.common.dto;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;

import java.time.LocalDate;
import java.util.Map;

@Data
public class UserProfileCreatorDto implements CreateUpdateDto<String> {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthday;
    private boolean activated;
    private String address;
    private String phoneNumber;
    private String username;
    private String email;
    private String displayName;
    private String password;
    private String repeatPassword;
    private Map<String, Object> additionalAttributes;
}
