package tech.corefinance.userprofile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

import java.sql.Types;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_session")
@Data
public class LoginSession implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "login_time")
    private LocalDateTime loginTime;
    @Column(name = "refresh_token")
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String refreshToken;
    @Column(name = "login_token")
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String loginToken;
    @Column(name = "valid_token")
    private boolean validToken;
    @Column(name = "verify_key")
    private String verifyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    @JsonBackReference
    @JsonIgnore
    private UserProfile userProfile;

    public LoginSession() {
        this.loginTime = LocalDateTime.now();
    }
}
