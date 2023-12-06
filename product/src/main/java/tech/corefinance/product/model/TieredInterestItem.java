package tech.corefinance.product.model;

import lombok.Data;

@Data
public class TieredInterestItem {
    /**
     * Value can be number of days or balance.
     */
    private double fromValue;
    /**
     * Value can be number of days or balance.
     */
    private double toValue;
    /**
     * Percentage value.
     */
    private double interestPercentage;
}
