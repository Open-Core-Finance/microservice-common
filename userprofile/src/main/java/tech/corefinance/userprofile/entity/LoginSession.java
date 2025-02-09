package tech.corefinance.userprofile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.userprofile.common.entity.CommonLoginSession;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "login_session")
@Data
public class LoginSession extends CommonLoginSession<UserProfile> implements GenericModel<String>, CreateUpdateDto<String> {
}
