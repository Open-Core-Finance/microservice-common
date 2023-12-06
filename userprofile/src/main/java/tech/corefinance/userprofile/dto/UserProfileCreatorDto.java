package tech.corefinance.userprofile.dto;

import lombok.Data;
import tech.corefinance.common.enums.Gender;
import tech.corefinance.common.model.CreateUpdateDto;

import java.time.LocalDate;

@Data
public class UserProfileCreatorDto implements CreateUpdateDto<String> {
    private String entityId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private boolean activated;
    private String address;
    private String phoneNumber;
    private String username;
    private String email;
    private String displayName;
    private String password;
    private String repeatPassword;
}
