package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.PenaltyCalculationMethod;
import tech.corefinance.product.common.model.CurrencyValue;
import tech.corefinance.product.common.model.ValueConstraint;

import java.util.List;

/**
 * PenaltySetting<br/>
 * How is the penalty rate charged? => % per year.
 */
@Data
public class AccountPenaltySetting {

    private boolean sameConstraintForAllCurrency;

    private PenaltyCalculationMethod calculationMethod;
    /**
     * Penalty Tolerance Period X Days.
     */
    private Integer penaltyTolerancePeriod;
    /**
     * Penalty Rate Constraints (%).
     */
    private List<CurrencyValue> penaltyRateValues;
}
