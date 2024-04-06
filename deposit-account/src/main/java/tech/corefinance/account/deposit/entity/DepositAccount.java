package tech.corefinance.account.deposit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.account.common.entity.Account;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.account.common.model.DepositAccountInterestRate;
import tech.corefinance.common.annotation.CustomAuditor;
import tech.corefinance.common.audit.EntityBasicUserAuditorListener;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.audit.EntityZonedDateTimeAuditListener;
import tech.corefinance.common.enums.CustomAuditorField;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.enums.FrequencyOptionYearly;
import tech.corefinance.product.common.model.CurrencyValue;
import tech.corefinance.product.common.model.DepositProductInterestRate;
import tech.corefinance.product.common.model.DepositLimit;
import tech.corefinance.product.common.model.WithdrawalLimit;

import java.util.List;

@Entity
@Table(name = "deposit_account")
@Data
@EqualsAndHashCode(callSuper = true)
@CustomAuditor(createdByType = CustomAuditorField.BASIC_USER_JSON, lastModifiedByType = CustomAuditorField.BASIC_USER_JSON)
@EntityListeners({EntityBasicUserAuditorListener.class, EntityZonedDateTimeAuditListener.class, EntityDeleteListener.class})
public class DepositAccount extends Account {

    @Column(name = "allow_deposit_after_maturity_date")
    private boolean allowDepositAfterMaturityDate;

    /**
     * Interest Rate.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "interest_rate")
    private DepositAccountInterestRate interestRate;

    // Internal control
    @Column(name = "days_to_set_to_dormant")
    private Integer daysToSetToDormant;

    /**
     * Deposit transaction limits.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "deposit_limits")
    private List<DepositLimit> depositLimits;
    /**
     * Withdrawal Limits.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "withdrawal_limits")
    private List<WithdrawalLimit> withdrawalLimits;
    /**
     * Early Closure Period.
     */
    @Column(name = "early_closure_period")
    private Integer earlyClosurePeriod;

    @Column(name = "allow_overdrafts")
    private Boolean allowOverdrafts;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "overdrafts_interest")
    private DepositProductInterestRate overdraftsInterest;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "max_overdraft_limit")
    private List<CurrencyValue> maxOverdraftLimit;
    @Enumerated(EnumType.STRING)
    @Column(name = "overdrafts_under_credit_arrangement_managed")
    private CreditArrangementManaged overdraftsUnderCreditArrangementManaged;

    @Column(name = "enable_term_deposit")
    private boolean enableTermDeposit;
    @Enumerated(EnumType.STRING)
    @Column(name = "term_unit")
    private FrequencyOptionYearly termUnit;
    @Column(name = "term_length")
    private Integer termLength;

    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    @Column(name = "customer_id")
    private long customerId;
}
