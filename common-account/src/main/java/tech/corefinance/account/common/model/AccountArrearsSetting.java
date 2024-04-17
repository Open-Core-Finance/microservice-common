package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.ArrearsDaysCalculatedFrom;
import tech.corefinance.product.common.model.CurrencyValue;
import tech.corefinance.product.common.model.ValueConstraint;

import java.util.List;

@Data
public class AccountArrearsSetting {
    private boolean sameConstraintForAllCurrency;
    /**
     * Arrears Tolerance Period in Days.
     */
    private List<CurrencyValue> tolerancePeriods;
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
    private List<CurrencyValue> toleranceAmounts;
    /**
     * With a floor (minimum).
     */
    private List<CurrencyValue> floors;
}
