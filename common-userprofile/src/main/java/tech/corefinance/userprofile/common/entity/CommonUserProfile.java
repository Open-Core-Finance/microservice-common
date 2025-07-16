package tech.corefinance.userprofile.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.common.model.GenericModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@MappedSuperclass
@Data
public abstract class CommonUserProfile<R extends CommonRole<?>> implements GenericModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    private String gender;
    private LocalDate birthday;
    @Column(name = "activated")
    private boolean activated;
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String username;
    private String email;
    @Column(name = "display_name")
    private String displayName;
    @JsonIgnore
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_profile_role", joinColumns = @JoinColumn(name = "user_profile_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<R> commonRoles;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_attributes")
    private Map<String, Object> additionalAttributes;
}
