package tech.corefinance.account.loan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.entity.Account;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.common.annotation.CustomAuditor;
import tech.corefinance.common.audit.EntityBasicUserAuditorListener;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.audit.EntityZonedDateTimeAuditListener;
import tech.corefinance.common.enums.CustomAuditorField;

@Entity
@Table(name = "loan_account")
@Data
@EqualsAndHashCode(callSuper = true)
@CustomAuditor(createdByType = CustomAuditorField.BASIC_USER_JSON, lastModifiedByType = CustomAuditorField.BASIC_USER_JSON)
@EntityListeners({EntityBasicUserAuditorListener.class, EntityZonedDateTimeAuditListener.class, EntityDeleteListener.class})
public class LoanAccount extends Account {
    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    @Column(name = "customer_id")
    private long customerId;
}
