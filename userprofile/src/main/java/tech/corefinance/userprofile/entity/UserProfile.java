package tech.corefinance.userprofile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_profile")
@Data
public class UserProfile extends CommonUserProfile<Role> implements GenericModel<String> {

    @Column(name = "finance_level")
    private Integer financeLevel;
}
