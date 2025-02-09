package tech.corefinance.userprofile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.userprofile.common.entity.CommonRole;

@Entity
@Table(name = "role")
public class Role extends CommonRole<UserProfile> implements GenericModel<String> {
}
