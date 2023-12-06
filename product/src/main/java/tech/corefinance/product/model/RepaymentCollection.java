package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.PrePaymentRecalculation;
import tech.corefinance.product.enums.RepaymentType;

import java.util.List;

@Data
public class RepaymentCollection {
    /**
     * Horizontal or Vertical.
     */
    private Boolean repaymentHorizontal;
    /**
     * Accept Pre-Payments. Whether interest that's not yet applied in the account can be paid in advance.
     */
    private Boolean acceptInterestPrePayments;
    /**
     * Accept Pre-Payments. Whether interest that's not yet applied in the account can be paid in advance.
     */
    private Boolean acceptPostdatedPrePayments;
    /**
     * Dynamic term loan. Auto Apply Interest on Pre-Payment.
     */
    private Boolean autoApplyInterestPrePayment;
    /**
     * Dynamic term loan Pre-Payment Recalculation.
     */
    private PrePaymentRecalculation prePaymentRecalculation;
    /**
     *  Arrange repayment types according to which should be paid first and last on partial or over-payments.
     */
    private List<RepaymentType> repaymentTypesOrder;
}
