package tech.corefinance.common.test.support.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import tech.corefinance.common.dto.UserRoleDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.GenericModel;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@Data
public class UserTest implements GenericModel<String> {

    /**
     * Generated.
     */
    private static final long serialVersionUID = 1701268097819323674L;

    @Id
    private String id;
    @NotEmpty(message = "username_must_not_null")
    private String username;
    @NotEmpty(message = "email_must_not_null")
    private String email;
    @NotEmpty(message = "firstName_must_not_null")
    private String firstName;
    private String middleName;
    @NotEmpty(message = "lastName_must_not_null")
    private String lastName;
    @NotEmpty(message = "displayName_must_not_null")
    private String displayName;
    private String password;

    @Transient
    private String repeatPassword;

    private String phoneNumber;

    @NotNull(message = "gender_must_not_null")
    private String gender;
    private LocalDate birthday;
    private String address;
    private List<UserRoleDto> rolesInSchools;
    private String creatorId;
    private String creatorDisplayName;
    private String lastModifyUserId;
    private String lastModifyUserDisplayName;
    private Set<AppPlatform> usagePlatform;

    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;

    public UserTest() {
        rolesInSchools = new LinkedList<>();
        usagePlatform = new HashSet<>();
    }

    public UserTest(String email, String firstName, String middleName, String lastName, String password, String phoneNumber) {
        this(email, firstName, middleName, lastName, password);
        this.phoneNumber = phoneNumber;
    }

    public UserTest(String email, String firstName, String middleName, String lastName, String password) {
        this();
        this.email = email;
        this.username = email;
        setFirstName(firstName);
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
    }


    public void addRoleInSchool(UserRoleDto userRoleDto) {
        getRolesInSchools().add(userRoleDto);
    }

    public boolean containsRoleForSchool(UserRoleDto userRoleDto) {
        return getRolesInSchools().contains(userRoleDto);
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        if (this.firstName != null && StringUtils.isBlank(this.displayName)) {
            this.displayName = this.firstName;
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserTest that) {
            return Objects.equals(this.getId(), that.getId()) && Objects.equals(this.email, that.email);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId()) + Objects.hashCode(getEmail());
    }
}
