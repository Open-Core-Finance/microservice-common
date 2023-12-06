package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.ArrearsDaysCalculatedFrom;

@Data
public class ArrearsSetting {
    /**
     * Arrears Tolerance Period in Days.
     */
    private ValueConstraint tolerancePeriod;
    /**
     * Include/Exclude Non-Working Days in Arrears Tolerance Period and Penalty Calculation Method.
     */
    private boolean includeNonWorkingDay;
    /**
     * Arrears Days Calculated From.
     */
    private ArrearsDaysCalculatedFrom daysCalculatedFrom;
    /**
     * Arrears Tolerance Amount (% of Outstanding Principal).
     */
    private ValueConstraint toleranceAmount;
    /**
     * With a floor (minimum).
     */
    private Double floor;
}
