package tech.corefinance.product.model;

import lombok.Data;

/**
 * PenaltySetting<br/>
 * How is the penalty rate charged? => % per year.
 */
@Data
public class PenaltySetting {
    /**
     * 1 => No Penalty
     * 2 => Overdue Principal * # of Late Days * Penalty Rate
     * 3 => (Overdue Principal + Overdue Interest) * # of Late Days * Penalty Rate
     * 4 => Outstanding Principal * # of Late Days * Penalty Rate
     */
    private byte calculationMethod;
    /**
     * Penalty Tolerance Period X Days.
     */
    private Integer penaltyTolerancePeriod;
    /**
     * Penalty Rate Constraints (%).
     */
    private ValueConstraint penaltyRateConstraint;
}
