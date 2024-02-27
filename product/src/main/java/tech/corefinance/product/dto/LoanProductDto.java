package tech.corefinance.product.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.product.common.enums.CreditArrangementManaged;
import tech.corefinance.product.common.model.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoanProductDto extends ProductDto {
    private List<ValueConstraint> loanValues;
    private CreditArrangementManaged underCreditArrangementManaged;
    private LoanProductInterestRate interestRate;
    private RepaymentScheduling repaymentScheduling;
    private RepaymentCollection repaymentCollection;
    private ArrearsSetting arrearsSetting;
    private PenaltySetting penaltySetting;

    private boolean closeDormantAccounts;
    private boolean lockArrearsAccounts;
    private boolean capCharges;

    private Double percentSecurityPerLoan;
    private boolean enableGuarantors;
    private boolean enableCollateral;
}
