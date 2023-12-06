package tech.corefinance.product.model;

import lombok.Data;

@Data
public class RepaymentsScheduleEditing {

    private boolean adjustPaymentDates;
    private boolean adjustPrincipalPaymentSchedule;
    private boolean adjustNumberInstallments;
    private boolean configurePaymentHolidays;
}
