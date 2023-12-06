package tech.corefinance.userprofile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.model.GenericModel;

import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role implements GenericModel<String> {

    @Id
    private String id;
    private String name;
    @Column(name = "tenant_id")
    private String tenantId;

    @OneToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserProfile> userProfiles;
}
