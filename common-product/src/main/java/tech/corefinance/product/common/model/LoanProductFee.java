package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.DepositProductFeeType;
import tech.corefinance.product.common.enums.LoanFeePaymentType;
import tech.corefinance.product.common.enums.LoanProductFeeType;
import tech.corefinance.product.common.enums.MonthlyPayOption;

@Data
public class LoanProductFee {

    private boolean activated;
    private String id;
    private String name;
    private LoanProductFeeType type;
    private LoanFeePaymentType feePaymentType;
    private Double amount;
    private String currencyId;
    private Boolean requiredFeeApplication;
}
