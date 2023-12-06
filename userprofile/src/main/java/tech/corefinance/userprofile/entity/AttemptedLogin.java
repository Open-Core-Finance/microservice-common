package tech.corefinance.userprofile.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

import java.time.LocalDate;

@Entity
@Table(name = "attempted_login")
@Data
@NoArgsConstructor
public class AttemptedLogin implements GenericModel<String>, CreateUpdateDto<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String account;
    @Column(name = "ip_address")
    private String ipAddress;
    private LocalDate date;
    private boolean enabled;
    @Column(name = "user_agent")
    private String userAgent;
    @Column(name = "device_token")
    private String deviceToken;
    @Column(name = "client_app_id")
    private String clientAppId;
    @Enumerated(EnumType.STRING)
    @Column(name = "app_platform")
    private AppPlatform appPlatform;
    @Column(name = "app_version")
    @JdbcTypeCode(SqlTypes.JSON)
    private AppVersion appVersion;

    public AttemptedLogin(String account, String ipAddress, String userAgent, String deviceToken, String clientAppId,
                          AppPlatform appPlatform, AppVersion appVersion) {
        this.account = account;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.deviceToken = deviceToken;
        this.date = LocalDate.now();
        this.enabled = true;
        this.clientAppId = clientAppId;
        this.appPlatform = appPlatform;
        this.appVersion = appVersion;
    }
}
