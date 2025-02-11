package tech.corefinance.userprofile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.userprofile.common.entity.CommonRole;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role")
@Data
public class Role extends CommonRole<UserProfile> implements GenericModel<String> {
}
