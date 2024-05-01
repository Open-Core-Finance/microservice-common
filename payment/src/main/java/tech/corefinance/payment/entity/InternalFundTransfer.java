package tech.corefinance.payment.entity;

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

@Table(name = "internal_fund_transfer")
@Entity
@Data
@CustomAuditor(createdByType = CustomAuditorField.BASIC_USER_JSON, lastModifiedByType = CustomAuditorField.BASIC_USER_JSON)
@EntityListeners({EntityBasicUserAuditorListener.class, EntityZonedDateTimeAuditListener.class, EntityDeleteListener.class})
public class InternalFundTransfer implements GenericModel<String>, CreateUpdateDto<String>, ModifiedDateTrackedEntity<ZonedDateTime>,
        AuditableEntity<BasicUserDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "from_customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType fromCustomerType;
    @Column(name = "from_customer_id")
    private long fromCustomerId;
    @Column(name = "from_account_id")
    private String fromAccountId;
    @Column(name = "from_account_type")
    @Enumerated(EnumType.STRING)
    private AccountType fromAccountType;
    @Column(name = "from_currency")
    private String fromCurrency;
    @Column(name = "from_amount")
    private double fromAmount;

    @Column(name = "to_customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType toCustomerType;
    @Column(name = "to_customer_id")
    private long toCustomerId;
    @Column(name = "to_account_id")
    private String toAccountId;
    @Column(name = "to_account_type")
    @Enumerated(EnumType.STRING)
    private AccountType toAccountType;
    @Column(name = "to_currency")
    private String toCurrency;
    @Column(name = "to_amount")
    private double toAmount;

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
}
