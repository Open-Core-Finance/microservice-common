package tech.corefinance.userprofile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.enums.Gender;
import tech.corefinance.common.model.GenericModel;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user_profile")
@Data
public class UserProfile implements GenericModel<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
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
    @JoinTable(
            name = "user_profile_role",
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
