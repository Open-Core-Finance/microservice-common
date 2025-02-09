package tech.corefinance.userprofile.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Map;

@MappedSuperclass
@Data
public abstract class CommonLoginSession<U extends CommonUserProfile<?>> implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private U userProfile;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_info")
    private Map<String, Object> additionalInfo;

    @Column(name = "input_account")
    private String inputAccount;
    @Column(name = "input_password")
    private String inputPassword;

    public CommonLoginSession() {
        this.loginTime = LocalDateTime.now();
    }

}
