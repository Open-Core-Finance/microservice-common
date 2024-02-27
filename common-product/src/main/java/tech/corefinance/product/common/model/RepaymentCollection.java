package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.PrePaymentRecalculation;
import tech.corefinance.product.common.enums.RepaymentType;

import java.util.List;

@Data
public class RepaymentCollection {
    /**
     * Horizontal or Vertical.
     */
    private Boolean repaymentHorizontal;
    private boolean acceptPrePayments;
    /**
     * Dynamic term loan. Auto Apply Interest on Pre-Payment.
     */
    private Boolean autoApplyInterestPrePayments;
    /**
     * Dynamic term loan Pre-Payment Recalculation.
     */
    private PrePaymentRecalculation prePaymentRecalculation;

    private boolean acceptPrepaymentFutureInterest;
    /**
     *  Arrange repayment types according to which should be paid first and last on partial or over-payments.
     */
    private List<RepaymentType> repaymentTypesOrder;
}
