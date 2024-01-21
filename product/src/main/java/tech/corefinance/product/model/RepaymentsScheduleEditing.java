package tech.corefinance.product.model;

import lombok.Data;

@Data
public class RepaymentsScheduleEditing {

    private boolean adjustPaymentDates;
    private boolean adjustPrincipalPaymentSchedule;
    // Dynamic term loan
    private boolean adjustNumberInstallments;
    // Dynamic term loan & Fixed term loan
    private boolean configurePaymentHolidays;

    // Not Dynamic term loan
    private boolean adjustFeePaymentSchedule;
    // Not Dynamic term loan
    private boolean adjustPenaltyPaymentSchedule;

    // Fixed term loan
    private boolean adjustInterestPaymentSchedule;
}
