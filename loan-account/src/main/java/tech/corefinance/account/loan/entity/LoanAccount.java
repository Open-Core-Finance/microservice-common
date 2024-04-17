package tech.corefinance.account.loan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.account.common.entity.Account;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.account.common.model.AccountArrearsSetting;
import tech.corefinance.account.common.model.AccountPenaltySetting;
import tech.corefinance.account.common.model.AccountRepaymentScheduling;
import tech.corefinance.account.common.model.LoanAccountInterestRate;
import tech.corefinance.common.annotation.CustomAuditor;
import tech.corefinance.common.audit.EntityBasicUserAuditorListener;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.audit.EntityZonedDateTimeAuditListener;
import tech.corefinance.common.enums.CustomAuditorField;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.model.*;

import java.util.List;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_fees")
    private List<LoanProductFee> productFees;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "loan_applied_values")
    private List<CurrencyValue> loanAppliedValues;

    @Enumerated(EnumType.STRING)
    @Column(name = "under_credit_arrangement_managed")
    private CreditArrangementManaged underCreditArrangementManaged;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "account_interest_rate")
    private LoanAccountInterestRate accountInterestRate;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "account_repayment_scheduling")
    private AccountRepaymentScheduling accountRepaymentScheduling;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "repayment_collection")
    private RepaymentCollection repaymentCollection;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "account_arrears_setting")
    private AccountArrearsSetting accountArrearsSetting;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "account_penalty_setting")
    private AccountPenaltySetting accountPenaltySetting;

    @Column(name = "close_dormant_accounts")
    private boolean closeDormantAccounts;
    @Column(name = "lock_arrears_accounts")
    private boolean lockArrearsAccounts;
    @Column(name = "cap_charges")
    private boolean capCharges;

    @Column(name = "percent_security_per_loan")
    private Double percentSecurityPerLoan;
    @Column(name = "enable_guarantors")
    private boolean enableGuarantors;
    @Column(name = "enable_collateral")
    private boolean enableCollateral;
}
