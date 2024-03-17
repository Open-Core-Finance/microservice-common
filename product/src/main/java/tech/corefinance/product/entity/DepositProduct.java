package tech.corefinance.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.enums.FrequencyOptionYearly;
import tech.corefinance.product.common.model.*;

import java.util.List;

/**
 * <b>CurrentAccount:</b> <br/>
 * A transactional account where a client may perform regular deposit and withdrawals,
 * accrue interest and may optionally be allowed to go into overdraft.<br/><br/>
 *
 * <b>Savings Account:</b> <br/>
 * Allows you to create accounts where clients can make deposits and withdrawals when they wish.
 * The interest is posted at the frequency you choose and accrued over time. It doesn't allow overdrafts.<br/><br/>
 *
 * <b>Fixed Deposit:</b> <br/>
 * As the name suggests, fixed deposits have a fixed term after which they should be withdrawn or closed.
 * With this type of product, clients are able to make deposits until the minimum opening balance has been reached.
 * At this point, you can begin the maturity period, during which they will be unable to deposit, but will be able to withdraw.
 * Before the maturity date, you have the option to undo maturity. <br/><br/>
 * <b>Savings Plan: </b><br/>Uses a maturity period like fixed deposits,
 * but once the minimum opening balance has been reached, they will still be able to make deposits,
 * even during the maturity period itself.
 * However, they will no longer be able to make deposits once the maturity period has ended. <br/><br/>
 *
 * <b>Savings Plan:</b> <br/>
 * Uses a maturity period like fixed deposits, but once the minimum opening balance has been reached,
 * they will still be able to make deposits, even during the maturity period itself.
 * However, they will no longer be able to make deposits once the maturity period has ended.<br/><br/>
 */
@Data
@Entity
@Table(name = "deposit_product")
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DepositProduct extends Product {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_fees")
    private List<DepositProductFee> productFees;

    @Column(name = "allow_deposit_after_maturity_date")
    private boolean allowDepositAfterMaturityDate;

    /**
     * Interest Rate.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "interest_rate")
    private DepositProductInterestRate interestRate;

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
    @Column(name = "min_term_length")
    private Integer minTermLength;
    @Column(name = "max_term_length")
    private Integer maxTermLength;
    @Column(name = "default_term_length")
    private Integer defaultTermLength;
}
