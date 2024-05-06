package tech.corefinance.balance.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.account.common.model.AccountType;
import tech.corefinance.common.annotation.CustomAuditor;
import tech.corefinance.common.audit.EntityBasicUserAuditorListener;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.audit.EntityZonedDateTimeAuditListener;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.enums.CustomAuditorField;
import tech.corefinance.common.model.AuditableEntity;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.time.ZonedDateTime;

@Table(name = "account_balance")
@Entity
@Data
@CustomAuditor(createdByType = CustomAuditorField.BASIC_USER_JSON, lastModifiedByType = CustomAuditorField.BASIC_USER_JSON)
@EntityListeners({EntityBasicUserAuditorListener.class, EntityZonedDateTimeAuditListener.class, EntityDeleteListener.class})
public class AccountBalance implements GenericModel<AccountBalanceId>, CreateUpdateDto<AccountBalanceId>, ModifiedDateTrackedEntity<ZonedDateTime>,
        AuditableEntity<BasicUserDto> {
    @EmbeddedId
    private AccountBalanceId id;

    @CreatedDate
    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    @CreatedBy
    @Column(name = "created_by")
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserDto createdBy;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;
    @CreatedBy
    @Column(name = "last_modified_by")
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserDto lastModifiedBy;

    @Column(name = "begin_balance")
    private double beginBalance;
    @Column(name = "end_balance")
    private double endBalance;
    @Column(name = "current_balance")
    private double currentBalance;
}
