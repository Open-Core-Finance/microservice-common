package tech.corefinance.userprofile.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.common.model.GenericModel;

import java.util.List;
import java.util.Map;

@MappedSuperclass
@Data
public abstract class CommonRole<U extends CommonUserProfile> implements GenericModel<String> {

    @Id
    private String id;
    private String name;
    @Column(name = "tenant_id")
    private String tenantId;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_profile_role", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_profile_id"))
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<U> userProfiles;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "additional_attributes")
    private Map<String, Object> additionalAttributes;
}
