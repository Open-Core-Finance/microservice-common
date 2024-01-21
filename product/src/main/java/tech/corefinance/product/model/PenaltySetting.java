package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.PenaltyCalculationMethod;

import java.util.List;

/**
 * PenaltySetting<br/>
 * How is the penalty rate charged? => % per year.
 */
@Data
public class PenaltySetting {
    private PenaltyCalculationMethod calculationMethod;
    /**
     * Penalty Tolerance Period X Days.
     */
    private Integer penaltyTolerancePeriod;
    /**
     * Penalty Rate Constraints (%).
     */
    private List<ValueConstraint> penaltyRateConstraints;
}
