package tech.corefinance.account.deposit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.account.common.entity.Account;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.account.common.model.DepositAccountInterestRate;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.enums.FrequencyOptionYearly;
import tech.corefinance.product.common.model.CurrencyValue;
import tech.corefinance.product.common.model.DepositLimit;
import tech.corefinance.product.common.model.DepositProductFee;
import tech.corefinance.product.common.model.WithdrawalLimit;

import java.util.List;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class GenericDepositAccount extends Account {

    @Column(name = "allow_deposit_after_maturity_date")
    private boolean allowDepositAfterMaturityDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "account_fees")
    private List<DepositProductFee> accountFees;

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
    private DepositAccountInterestRate overdraftsInterest;
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
