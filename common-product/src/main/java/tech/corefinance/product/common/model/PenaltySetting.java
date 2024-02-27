package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.PenaltyCalculationMethod;

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
