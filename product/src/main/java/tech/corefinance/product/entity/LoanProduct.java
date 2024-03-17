package tech.corefinance.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.model.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "loan_product")
public class LoanProduct extends Product {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_fees")
    private List<LoanProductFee> productFees;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "loan_values")
    private List<ValueConstraint> loanValues;

    @Enumerated(EnumType.STRING)
    @Column(name = "under_credit_arrangement_managed")
    private CreditArrangementManaged underCreditArrangementManaged;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "interest_rate")
    private LoanProductInterestRate interestRate;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "repayment_scheduling")
    private RepaymentScheduling repaymentScheduling;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "repayment_collection")
    private RepaymentCollection repaymentCollection;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "arrears_setting")
    private ArrearsSetting arrearsSetting;
    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "penalty_setting")
    private PenaltySetting penaltySetting;

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
