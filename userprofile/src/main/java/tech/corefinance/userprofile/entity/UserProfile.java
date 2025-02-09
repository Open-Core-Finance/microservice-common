package tech.corefinance.userprofile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;

@Entity
@Table(name = "user_profile")
public class UserProfile extends CommonUserProfile<Role> implements GenericModel<String> {

    @Column(name = "finance_level")
    private Integer financeLevel;
}
